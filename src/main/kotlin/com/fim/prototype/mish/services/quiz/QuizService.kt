package com.fim.prototype.mish.services.quiz

import com.fim.prototype.mish.exceptions.NotFoundException
import com.fim.prototype.mish.exceptions.ValidationException
import com.fim.prototype.mish.model.common.FilterBase
import com.fim.prototype.mish.model.entities.quiz.QuizEntity
import com.fim.prototype.mish.model.entities.quiz.QuizSubmissionRequest
import com.fim.prototype.mish.model.entities.quiz.QuizValidationResult
import com.fim.prototype.mish.model.entities.quiz.answers.AbstractAnswerData
import com.fim.prototype.mish.model.entities.quiz.questions.AbstractQuestionData
import com.fim.prototype.mish.repo.QuizRepo
import com.fim.prototype.mish.services.chapters.ChapterService
import com.fim.prototype.mish.services.quiz.validators.CreateQuizValidator
import com.fim.prototype.mish.utils.PageRequestData
import com.fim.prototype.mish.utils.PageResult
import org.springframework.stereotype.Service

@Service
class QuizService(
    private val quizRepo: QuizRepo,
    private val quizAnswersResultService: QuizAnswersResultService,
    private val chapterService: ChapterService,
    questionValidator: List<CreateQuizValidator>,
) {

    val questionValidators = questionValidator.associateBy { it.type }

    fun createQuiz(quiz: QuizEntity): QuizEntity {
        if (quiz.id != null) throw ValidationException("Quiz has id ${quiz.id}! To create quiz id must be null!")
        validateQuiz(quiz)

        return quizRepo.save(quiz)
    }

    fun updateQuiz(quiz: QuizEntity): QuizEntity {
        quiz.id?.let { getQuizById(it) }
        validateQuiz(quiz)

        return quizRepo.save(quiz)
    }

    fun deleteQuiz(quizId: String) {
        quizRepo.deleteById(quizId)
    }

    fun getQuizById(quizId: String, showAnswers: Boolean = false): QuizEntity {
        //TODO if showAnswers is true, check if the user has permissions to see the answers (if user is teacher)

        return quizRepo.getQuizById(quizId, showAnswers) ?: throw NotFoundException("Quiz with id $quizId not found!")
    }

    fun listQuizzes(pageRequest: PageRequestData, filter: FilterBase): PageResult<QuizEntity> {
        return quizRepo.listQuizzes(pageRequest, filter)
    }

    fun getAnswersResult(quizId: String, submission: QuizSubmissionRequest): QuizValidationResult {
        return quizAnswersResultService.getAnswersResult(quizId, submission)
    }

    private fun validateQuiz(quiz: QuizEntity): QuizEntity {
        if (quiz.name == null) throw ValidationException("Quiz name is not set!")
        quiz.chapterId?.let { chapterService.getChapterById(it) }
        validateQuestionAndAnswers(quiz.questions, quiz.answers)

        //TODO get user and validate if exists and if has permissions to create quiz
        return quiz
    }

    private fun validateQuestionAndAnswers(questions: List<AbstractQuestionData>, answers: List<AbstractAnswerData>) {
        if (questions.isEmpty()) throw ValidationException("Quiz must have at least one question!")
        if (answers.isEmpty()) throw ValidationException("Quiz must have at least one answer!")
        if (questions.size != answers.size) throw ValidationException("Number of questions and answers must be the same!")


        questions.forEachIndexed{ index, question ->
            val answer = answers[index]

            if (question.type != answer.type) throw ValidationException("Question type ${question.type} does not match answer type ${answer::type}!")

            questionValidators[question::class]?.validate(question, answer)
                ?: throw ValidationException("No validator found for question type ${question::class}!")
        }

    }
}
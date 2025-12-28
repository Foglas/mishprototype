package com.fim.prototype.mish.services.quiz

import com.fim.prototype.mish.exceptions.NotFoundException
import com.fim.prototype.mish.exceptions.ValidationException
import com.fim.prototype.mish.model.entities.quiz.QuizEntity
import com.fim.prototype.mish.model.entities.quiz.QuizSubmissionRequest
import com.fim.prototype.mish.model.entities.quiz.QuizValidationResult
import com.fim.prototype.mish.model.entities.quiz.questions.AbstractQuestionData
import com.fim.prototype.mish.repo.QuizRepo
import com.fim.prototype.mish.services.chapters.ChapterService
import com.fim.prototype.mish.services.quiz.questions.QuestionValidator
import org.springframework.stereotype.Service

@Service
class QuizService(
    private val quizRepo: QuizRepo,
    private val quizValidatorService: QuizValidatorService,
    private val chapterService: ChapterService,
    questionValidator: List<QuestionValidator>,
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

    fun validateAnswers(quizId: String, submission: QuizSubmissionRequest): QuizValidationResult {
        return quizValidatorService.validateAnswers(quizId, submission)
    }

    private fun validateQuiz(quiz: QuizEntity): QuizEntity {
        if (quiz.name == null) throw ValidationException("Quiz name is not set!")
        if (quiz.questions.isEmpty()) throw ValidationException("Quiz must have at least one question!")
        if (quiz.answers.isEmpty()) throw ValidationException("Quiz must have at least one answer!")
        if (quiz.questions.size != quiz.answers.size) throw ValidationException("Number of questions and answers must be the same!")

        quiz.chapterId?.let { chapterService.getChapterById(it) }

        quiz.questions.forEach { validateQuestion(it) }
        //TODO get user and validate if exists and if has permissions to create quiz
        return quiz
    }

    private fun validateQuestion(question: AbstractQuestionData) {
        questionValidators[question::class]?.validate(question)
            ?: throw ValidationException("No validator found for question type ${question::class}!")
    }
}
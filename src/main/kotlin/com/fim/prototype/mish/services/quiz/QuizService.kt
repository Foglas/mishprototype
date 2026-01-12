package com.fim.prototype.mish.services.quiz

import com.fim.prototype.mish.cache.InMemoryCache
import com.fim.prototype.mish.exceptions.NotFoundException
import com.fim.prototype.mish.exceptions.ValidationException
import com.fim.prototype.mish.model.common.filters.FilterBase
import com.fim.prototype.mish.model.common.UserTimeAction
import com.fim.prototype.mish.model.entities.quiz.QuickQuizEntity
import com.fim.prototype.mish.model.entities.quiz.QuizEntity
import com.fim.prototype.mish.model.entities.quiz.answers.AbstractAnswerData
import com.fim.prototype.mish.model.entities.quiz.questions.AbstractQuestionData
import com.fim.prototype.mish.repo.QuizRepo
import com.fim.prototype.mish.security.service.AuthenticationService
import com.fim.prototype.mish.services.chapters.ChapterService
import com.fim.prototype.mish.services.quiz.validators.CreateQuizValidator
import com.fim.prototype.mish.utils.PageRequestData
import com.fim.prototype.mish.utils.PageResult
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class QuizService(
    private val quizRepo: QuizRepo,
    private val chapterService: ChapterService,
    private val authenticationService: AuthenticationService,
    private val inMemoryCache: InMemoryCache<String, UserTimeAction<Instant>>,
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

    fun getQuizById(quizId: String, showAnswers: Boolean = false, startQuiz: Boolean = false): QuizEntity {
        //TODO if showAnswers is true, check if the user has permissions to see the answers (if user is teacher)

        val userId = authenticationService.getCurrentUser().userId
        val startTime = Instant.now()

        val quiz = quizRepo.getQuizById(quizId, showAnswers) ?: throw NotFoundException("Quiz with id $quizId not found!")
        if (startQuiz) inMemoryCache.put(userId, UserTimeAction(userId,startTime, startTime.plus(quiz.timeLimit.toLong(), ChronoUnit.SECONDS)))

        return quiz
    }

    fun getQuickQuizById(quizId: String): QuickQuizEntity {
        return quizRepo.getQuickQuizById(quizId) ?: throw NotFoundException("Quiz with id $quizId not found!")
    }

    fun listQuizzes(pageRequest: PageRequestData, filter: FilterBase): PageResult<QuizEntity> {
        return quizRepo.listQuizzes(pageRequest, filter)
    }

    private fun validateQuiz(quiz: QuizEntity): QuizEntity {
        if (quiz.name == null) throw ValidationException("Quiz name is not set!")
        quiz.chapterId?.let { chapterService.getChapterById(it) }
        validateQuestionAndAnswers(quiz.questions, quiz.answers)

        //TODO get user and validate if exists and if has permissions to create quiz
        //TODO create UnauthorizedException and throw it here
        quiz.creatorId = authenticationService.getCurrentUser().userId
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
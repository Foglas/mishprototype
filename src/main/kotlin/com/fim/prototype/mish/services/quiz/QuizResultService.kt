package com.fim.prototype.mish.services.quiz

import com.fim.prototype.mish.cache.RedisCache
import com.fim.prototype.mish.exceptions.NotFoundException
import com.fim.prototype.mish.exceptions.ValidationException
import com.fim.prototype.mish.model.common.StartQuizAction
import com.fim.prototype.mish.model.common.UserTimeAction
import com.fim.prototype.mish.model.common.filters.QuizResultFilter
import com.fim.prototype.mish.model.entities.quiz.*
import com.fim.prototype.mish.repo.QuizResultRepo
import com.fim.prototype.mish.security.service.AuthenticationService
import com.fim.prototype.mish.utils.PageRequestData
import com.fim.prototype.mish.utils.PageResult
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class QuizResultService(
    private val authenticationService: AuthenticationService,
    private val quizResultRepo: QuizResultRepo,
    private val inMemoryCache: RedisCache,
    private val quizAnswersResultService: QuizAnswersResultService,
) {

    fun create(quizResult: QuizValidationResult): QuizValidationResultWithUser {
        return quizResultRepo.save(quizResult.toQuizValidationResultWithUser(authenticationService.getCurrentUser().userId))
    }

    fun getQuizResult(quizId: String): QuizValidationResult {
       return quizResultRepo.getResultById(quizId) ?: throw NotFoundException("Quiz result for id $quizId was not found!")
    }

    fun getQuickQuizResult(quizId: String): QuickQuizResult {
        return quizResultRepo.getQuickResultById(quizId) ?: throw NotFoundException("Quiz result for id $quizId was not found!")
    }

    fun listQuizResults(pageRequestData: PageRequestData, filter: QuizResultFilter): PageResult<QuickQuizResult> {
        return quizResultRepo.list(pageRequestData, filter)
    }

    fun getAnswersResult(quizId: String, submission: QuizSubmissionRequest): QuizValidationResult {
        val quizEnd = inMemoryCache.delete(authenticationService.getCurrentUser().userId, StartQuizAction::class) ?: throw ValidationException("Quiz was not started properly!")

        //TODO maybe time per question? To accept question filled before quizEnd but received after quizEnd
        if (quizEnd.time.isBefore(Instant.now()) && quizEnd.hasTimeLimit) throw ValidationException("Quiz time limit has expired!")

        val result = quizAnswersResultService.getAnswersResult(quizId, submission)

        return create(result).toQuizValidationResult()
    }
}
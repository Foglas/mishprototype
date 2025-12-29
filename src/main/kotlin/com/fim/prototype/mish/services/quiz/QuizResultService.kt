package com.fim.prototype.mish.services.quiz

import com.fim.prototype.mish.cache.InMemoryCache
import com.fim.prototype.mish.exceptions.NotFoundException
import com.fim.prototype.mish.exceptions.ValidationException
import com.fim.prototype.mish.model.common.FilterBase
import com.fim.prototype.mish.model.common.UserTimeAction
import com.fim.prototype.mish.model.entities.quiz.*
import com.fim.prototype.mish.repo.QuizResultRepo
import com.fim.prototype.mish.security.service.CurrentUserService
import com.fim.prototype.mish.utils.PageRequestData
import com.fim.prototype.mish.utils.PageResult
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class QuizResultService(
    private val currentUserService: CurrentUserService,
    private val quizResultRepo: QuizResultRepo,
    private val inMemoryCache: InMemoryCache<String, UserTimeAction<Instant>>,
    private val quizAnswersResultService: QuizAnswersResultService,
) {

    fun create(quizResult: QuizValidationResult): QuizValidationResultWithUser {
        return quizResultRepo.save(quizResult.toQuizValidationResultWithUser(currentUserService.getCurrentUser().userId))
    }

    fun getQuizResult(quizId: String): QuizValidationResult {
       return quizResultRepo.getResultById(quizId) ?: throw NotFoundException("Quiz result for id $quizId was not found!")
    }

    fun getQuickQuizResult(quizId: String): QuickQuizResult {
        return quizResultRepo.getQuickResultById(quizId) ?: throw NotFoundException("Quiz result for id $quizId was not found!")
    }

    fun listQuizResults(pageRequestData: PageRequestData, filter: FilterBase): PageResult<QuickQuizResult> {
        return PageResult(emptyList(), 0,0)
    }

    fun getAnswersResult(quizId: String, submission: QuizSubmissionRequest): QuizValidationResult {
        val quizEnd = inMemoryCache.delete(currentUserService.getCurrentUser().userId)?.data ?: throw ValidationException("Quiz was not started properly!")

        //TODO maybe time per question? To accept question filled before quizEnd but received after quizEnd
        if (quizEnd.isBefore(Instant.now())) throw ValidationException("Quiz time limit has expired!")

        val result = quizAnswersResultService.getAnswersResult(quizId, submission)

        return create(result).toQuizValidationResult()
    }
}
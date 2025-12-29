package com.fim.prototype.mish.controllers

import com.fim.prototype.mish.model.common.filters.QuizResultFilter
import com.fim.prototype.mish.model.entities.quiz.QuickQuizResult
import com.fim.prototype.mish.model.entities.quiz.QuizSubmissionRequest
import com.fim.prototype.mish.model.entities.quiz.QuizValidationResult
import com.fim.prototype.mish.properties.PageProperties
import com.fim.prototype.mish.services.quiz.QuizResultService
import com.fim.prototype.mish.utils.PageRequestData
import com.fim.prototype.mish.utils.PageResult
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/api/quiz-result")
class QuizResultController(
    private val pageProperties: PageProperties,
    private val quizResultService: QuizResultService,
) {

    @GetMapping("/{id}")
    fun getQuizResult(@PathVariable("id") quizId: String): QuizValidationResult {
        return quizResultService.getQuizResult(quizId)
    }

    @GetMapping("{id}/quick")
    fun getQuickQuizResult(@PathVariable("id") quizId: String): QuickQuizResult {
        return quizResultService.getQuickQuizResult(quizId)
    }

    @GetMapping("/list")
    fun listQuizResults(
        @RequestParam page: Int,
        @RequestParam limit: Int = pageProperties.limit,
        @RequestParam orderBy: String? = null,
        @RequestParam sortDirection: Sort.Direction = pageProperties.sortDirection,
        @RequestParam name: String? = null,
        @RequestParam creatorId: String? = null,
        @RequestParam createdFrom: Instant? = null,
        @RequestParam createdTo: Instant? = null,
        @RequestParam chapterId: String? = null,
        @RequestParam quizId: String? = null
    ): PageResult<QuickQuizResult> {
        return quizResultService.listQuizResults(PageRequestData(page, limit, orderBy, sortDirection), QuizResultFilter(chapterId, quizId, name, creatorId, createdFrom, createdTo))
    }

    @GetMapping("/{id}/validate-result")
    fun getAnswersResult(@PathVariable("id") quizId: String, @RequestBody answers: QuizSubmissionRequest): QuizValidationResult {
        return quizResultService.getAnswersResult(quizId, answers)
    }

}
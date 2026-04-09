package com.fim.prototype.mish.controllers

import com.fim.prototype.mish.exceptions.ValidationException
import com.fim.prototype.mish.model.common.filters.QuizResultFilter
import com.fim.prototype.mish.model.entities.quiz.QuickQuizResult
import com.fim.prototype.mish.model.entities.quiz.QuizSubmissionRequest
import com.fim.prototype.mish.model.entities.quiz.QuizValidationResult
import com.fim.prototype.mish.properties.PageProperties
import com.fim.prototype.mish.services.quiz.QuizResultService
import com.fim.prototype.mish.utils.PageRequestData
import com.fim.prototype.mish.utils.PageResult
import com.fim.prototype.mish.utils.logger
import org.springframework.data.domain.Sort
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/api/quiz-result")
class QuizResultController(
    private val pageProperties: PageProperties,
    private val quizResultService: QuizResultService,
) {

    val log = logger()

    @PreAuthorize("hasRole(T(com.fim.prototype.mish.security.data.Roles).STUDENT_ACTION)")
    @GetMapping("/{id}")
    fun getQuizResult(@PathVariable("id") quizId: String): QuizValidationResult {
        log.debug("QuizResultController:getQuizResult() - id: $quizId")
        return quizResultService.getQuizResult(quizId)
    }

    @PreAuthorize("hasRole(T(com.fim.prototype.mish.security.data.Roles).STUDENT_ACTION)")
    @GetMapping("{id}/quick")
    fun getQuickQuizResult(@PathVariable("id") quizId: String): QuickQuizResult {
        log.debug("QuizResultController:getQuickQuizResult() - id: $quizId")
        return quizResultService.getQuickQuizResult(quizId)
    }

    @PreAuthorize("hasRole(T(com.fim.prototype.mish.security.data.Roles).STUDENT_ACTION)")
    @GetMapping("/list")
    fun listQuizResults(
        @RequestParam page: Int,
        @RequestParam limit: Int? = null,
        @RequestParam orderBy: String? = null,
        @RequestParam sortDirection: Sort.Direction? = null,
        @RequestParam name: String? = null,
        @RequestParam creatorId: String? = null,
        @RequestParam createdFrom: Instant? = null,
        @RequestParam createdTo: Instant? = null,
        @RequestParam quizId: String? = null
    ): PageResult<QuickQuizResult> {
        log.debug("QuizResultController:listQuizResults() - from ${createdFrom} - to: ${createdTo}")
        return quizResultService.listQuizResults(PageRequestData(page, limit?: pageProperties.limit, orderBy, sortDirection ?: pageProperties.sortDirection), QuizResultFilter(quizId, name, creatorId, createdFrom, createdTo))
    }

    @PreAuthorize("hasRole(T(com.fim.prototype.mish.security.data.Roles).STUDENT_ACTION)")
    @PostMapping("/validate-result")
    fun getAnswersResult(@RequestBody answers: QuizSubmissionRequest): QuizValidationResult {
        log.debug("QuizResultController:getAnswersResult()")
        return quizResultService.getAnswersResult(answers.quizId?: throw ValidationException("QuizId is not set!"), answers)
    }
}
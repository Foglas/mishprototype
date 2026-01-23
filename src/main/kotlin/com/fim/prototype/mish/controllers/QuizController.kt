package com.fim.prototype.mish.controllers

import com.fim.prototype.mish.model.common.filters.FilterBase
import com.fim.prototype.mish.model.entities.quiz.QuickQuizEntity
import com.fim.prototype.mish.model.entities.quiz.QuizEntity
import com.fim.prototype.mish.properties.PageProperties
import com.fim.prototype.mish.services.quiz.QuizService
import com.fim.prototype.mish.utils.PageRequestData
import com.fim.prototype.mish.utils.PageResult
import org.springframework.data.domain.Sort
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/api/quiz")
class QuizController(
    private val quizService: QuizService,
    private val pageProperties: PageProperties,
) {

    @PreAuthorize("hasRole(T(com.fim.prototype.mish.security.data.Roles).CREATE_QUIZ)")
    @PostMapping("/create")
    fun createQuiz(@RequestBody quiz: QuizEntity): QuizEntity {
       return quizService.createQuiz(quiz)
    }

    @PreAuthorize("hasRole(T(com.fim.prototype.mish.security.data.Roles).CREATE_QUIZ)")
    @PostMapping("/update")
    fun updateQuiz(@RequestBody quiz: QuizEntity): QuizEntity {
        return quizService.updateQuiz(quiz)
    }

    @PreAuthorize("hasRole(T(com.fim.prototype.mish.security.data.Roles).CREATE_QUIZ)")
    @DeleteMapping("/delete/{id}")
    fun deleteQuiz(@PathVariable("id") quizId: String){
        quizService.deleteQuiz(quizId)
    }

    @PreAuthorize("hasRole(T(com.fim.prototype.mish.security.data.Roles).CREATE_QUIZ)")
    @GetMapping("/{id}/all")
    fun getQuizById(@PathVariable("id") quizId: String): QuizEntity {
        return quizService.getQuizById(quizId, true)
    }

    @PreAuthorize("hasRole(T(com.fim.prototype.mish.security.data.Roles).STUDENT_ACTION)")
    @GetMapping("/{id}/quick")
    fun getQuickQuizById(@PathVariable("id") quizId: String): QuickQuizEntity {
        return quizService.getQuickQuizById(quizId)
    }

    @PreAuthorize("hasRole(T(com.fim.prototype.mish.security.data.Roles).STUDENT_ACTION)")
    @GetMapping("/{id}/questions")
    fun getQuestionsByQuizId(@PathVariable("id") quizId: String, @RequestParam startQuiz: Boolean = false): QuizEntity {
        return quizService.getQuizById(quizId, false, startQuiz)
    }

    @PreAuthorize("hasRole(T(com.fim.prototype.mish.security.data.Roles).STUDENT_ACTION)")
    @GetMapping("/list")
    fun listQuizzes(
        @RequestParam page: Int,
        @RequestParam limit: Int? = null,
        @RequestParam orderBy: String? = null,
        @RequestParam sortDirection: Sort.Direction?,
        @RequestParam name: String? = null,
        @RequestParam creatorId: String? = null,
        @RequestParam createdFrom: Instant? = null,
        @RequestParam createdTo: Instant? = null,
        ): PageResult<QuickQuizEntity> {
        return quizService.listQuizzes(PageRequestData(page, limit ?: pageProperties.limit, orderBy, sortDirection?: pageProperties.sortDirection), FilterBase(name, creatorId, createdFrom, createdTo))
    }
}
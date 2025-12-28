package com.fim.prototype.mish.controllers

import com.fim.prototype.mish.model.entities.quiz.QuizEntity
import com.fim.prototype.mish.model.entities.quiz.QuizSubmissionRequest
import com.fim.prototype.mish.model.entities.quiz.QuizValidationResult
import com.fim.prototype.mish.services.quiz.QuizService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/quiz")
class QuizController(
    private val quizService: QuizService
) {

    @PostMapping("/create")
    fun createQuiz(@RequestBody quiz: QuizEntity): QuizEntity {
       return quizService.createQuiz(quiz)
    }

    @PostMapping("/update")
    fun updateQuiz(@RequestBody quiz: QuizEntity): QuizEntity {
        return quizService.updateQuiz(quiz)
    }

    @DeleteMapping("/delete/{id}")
    fun deleteQuiz(@PathVariable("id") quizId: String){
        quizService.deleteQuiz(quizId)
    }

    @GetMapping("/{id}/all")
    fun getQuizById(@PathVariable("id") quizId: String): QuizEntity {
        return quizService.getQuizById(quizId, true)
    }

    @GetMapping("/{id}/questions")
    fun getQuestionsByQuizId(@PathVariable("id") quizId: String): QuizEntity {
        return quizService.getQuizById(quizId)
    }

    @GetMapping("/{id}/get-result")
    fun getAnswersResult(@PathVariable("id") quizId: String, @RequestBody answers: QuizSubmissionRequest): QuizValidationResult {
        return quizService.getAnswersResult(quizId, answers)
    }

}
package com.fim.prototype.mish.controllers

import com.fim.prototype.mish.model.entities.quiz.QuizEntity
import com.fim.prototype.mish.services.QuizService
import org.springframework.web.bind.annotation.*

@RestController("/api/quiz")
class QuizController(
    private val quizService: QuizService
) {

    @PostMapping("/create")
    fun createQuiz(quiz: QuizEntity) {
       quizService.createQuiz(quiz)
    }

    @DeleteMapping("/delete/{id}")
    fun deleteQuiz(@PathVariable("id") quizId: String){
        quizService.deleteQuiz(quizId)
    }

    @GetMapping("/quiz/{id}/all")
    fun getQuizById(@PathVariable("id") quizId: String): QuizEntity {
        return quizService.getQuizById(quizId)
    }

    @GetMapping("/quiz/{id}/questions")
    fun getQuestionsByQuizId(@PathVariable("id") quizId: String){
    }

    @GetMapping("/quiz/{id}/validate")
    fun validateAnswers(@PathVariable("id") quizId: String){
    }

}
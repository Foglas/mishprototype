package com.fim.prototype.mish.controllers

import com.fim.prototype.mish.model.entities.quiz.QuizEntity
import com.fim.prototype.mish.services.QuizService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

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

}
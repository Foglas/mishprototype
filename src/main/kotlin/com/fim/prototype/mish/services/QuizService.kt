package com.fim.prototype.mish.services

import com.fim.prototype.mish.exceptions.NotFoundException
import com.fim.prototype.mish.model.entities.quiz.QuizEntity
import com.fim.prototype.mish.repo.QuizRepo
import org.springframework.stereotype.Service

@Service
class QuizService(
    private val quizRepo: QuizRepo,
) {

    fun createQuiz(quiz: QuizEntity): QuizEntity {
        return quizRepo.save(quiz)
    }

    fun deleteQuiz(quizId: String){
        quizRepo.deleteById(quizId)
    }

    fun getQuizById(quizId: String, showAnswers: Boolean = false): QuizEntity {
        return quizRepo.getQuizById(quizId, showAnswers) ?: throw NotFoundException("Quiz with id $quizId not found!")
    }

    fun validateAnswers() {

    }
}
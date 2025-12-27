package com.fim.prototype.mish.services

import com.fim.prototype.mish.exceptions.NotFoundException
import com.fim.prototype.mish.model.entities.quiz.QuizEntity
import com.fim.prototype.mish.repo.interfaces.IQuizRepo
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class QuizService(
    private val quizRepo: IQuizRepo
) {

    fun createQuiz(quiz: QuizEntity): QuizEntity {
        return quizRepo.save(quiz)
    }

    fun deleteQuiz(quizId: String){
        quizRepo.deleteById(quizId)
    }

    fun getQuizById(quizId: String): QuizEntity {
        return quizRepo.findById(quizId).getOrNull() ?: throw NotFoundException("Quiz with id $quizId not found!")
    }
}
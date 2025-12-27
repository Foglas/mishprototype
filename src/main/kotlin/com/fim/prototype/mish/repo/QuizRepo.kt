package com.fim.prototype.mish.repo

import com.fim.prototype.mish.model.entities.quiz.QuizEntity
import com.fim.prototype.mish.repo.interfaces.IQuizRepo
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service

@Service
class QuizRepo(
    private val quizRepo: IQuizRepo,
    private val mongoTemplate: MongoTemplate
) {

    fun save(quiz: QuizEntity): QuizEntity {
        return quizRepo.save(quiz)
    }

    fun deleteById(quizId: String){
        quizRepo.deleteById(quizId)
    }

    fun getQuizById(quizId: String, showAnswers: Boolean = false): QuizEntity? {
        val query = Query(Criteria.where("_id").`is`(quizId))

        if (!showAnswers) {
            query.fields().exclude("answers")
        }

        return mongoTemplate.findOne(query, QuizEntity::class.java)
    }
}
package com.fim.prototype.mish.repo

import com.fim.prototype.mish.model.entities.quiz.QuickQuizResult
import com.fim.prototype.mish.model.entities.quiz.QuizValidationResult
import com.fim.prototype.mish.model.entities.quiz.QuizValidationResultWithUser
import com.fim.prototype.mish.repo.interfaces.IQuizResultRepo
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service

@Service
class QuizResultRepo(
    private val iQuizResultRepo: IQuizResultRepo,
    private val mongoTemplate: MongoTemplate
) {

    fun save(quizValidationResultWithUser: QuizValidationResultWithUser): QuizValidationResultWithUser {
        return iQuizResultRepo.save(quizValidationResultWithUser)
    }

    fun getResultById(quizId: String): QuizValidationResult? {
        val query = Query(Criteria.where("_id").`is`(quizId))
        query.fields().exclude("userId")

        return mongoTemplate.findOne(query, QuizValidationResult::class.java, "quizValidationResult")
    }

    fun getQuickResultById(quizId: String): QuickQuizResult?{
        val query = Query(Criteria.where("_id").`is`(quizId))
        query.fields()
            .exclude("questionResults")
            .exclude("questionScores")

        return mongoTemplate.findOne(query, QuickQuizResult::class.java, "quizValidationResult")
    }
}
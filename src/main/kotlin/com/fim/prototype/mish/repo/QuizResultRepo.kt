package com.fim.prototype.mish.repo

import com.fim.prototype.mish.model.common.filters.QuizResultFilter
import com.fim.prototype.mish.model.entities.quiz.QuickQuizResult
import com.fim.prototype.mish.model.entities.quiz.QuizValidationResult
import com.fim.prototype.mish.model.entities.quiz.QuizValidationResultWithUser
import com.fim.prototype.mish.repo.interfaces.IQuizResultRepo
import com.fim.prototype.mish.utils.PageRequestData
import com.fim.prototype.mish.utils.PageResult
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service

@Service
class QuizResultRepo(
    private val iQuizResultRepo: IQuizResultRepo,
    private val mongoTemplate: MongoTemplate,
    private val mongoBaseRepoUtils: MongoBaseRepoUtils
) {

    fun save(quizValidationResultWithUser: QuizValidationResultWithUser): QuizValidationResultWithUser {
        return iQuizResultRepo.save(quizValidationResultWithUser)
    }

    fun getResultById(quizId: String): QuizValidationResult? {
        val query = Query(Criteria.where("_id").`is`(quizId))
        query.fields().exclude("userId")

        return mongoTemplate.findOne(query, QuizValidationResult::class.java, "quizValidationResult")
    }

    fun list(pageRequest: PageRequestData, filter: QuizResultFilter): PageResult<QuickQuizResult> {
        val query = mongoBaseRepoUtils.createBaseFilterCriteriaAndReturnQuery(filter)
        query.fields()
            .exclude("questionResults")
            .exclude("questionScores")

        return mongoBaseRepoUtils.listPagedData(query, pageRequest, QuickQuizResult::class, MongoCollection.QUIZ_RESULT_ENTITY)
    }

    fun getQuickResultById(quizId: String): QuickQuizResult?{
        val query = Query(Criteria.where("_id").`is`(quizId))
        query.fields()
            .exclude("questionResults")
            .exclude("questionScores")

        return mongoTemplate.findOne(query, QuickQuizResult::class.java, "quizValidationResult")
    }
}
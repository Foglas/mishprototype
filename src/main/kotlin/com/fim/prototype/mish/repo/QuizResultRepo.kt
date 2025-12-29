package com.fim.prototype.mish.repo

import com.fim.prototype.mish.model.common.filters.QuizResultFilter
import com.fim.prototype.mish.model.entities.quiz.QuickQuizResult
import com.fim.prototype.mish.model.entities.quiz.QuizValidationResult
import com.fim.prototype.mish.model.entities.quiz.QuizValidationResultWithUser
import com.fim.prototype.mish.repo.interfaces.IQuizResultRepo
import com.fim.prototype.mish.utils.PageRequestData
import com.fim.prototype.mish.utils.PageResult
import com.fim.prototype.mish.utils.createPageRequest
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

    fun list(pageRequest: PageRequestData, filter: QuizResultFilter): PageResult<QuickQuizResult> {
        val query = Query().with(pageRequest.createPageRequest())
        query.fields()
            .exclude("questionResults")
            .exclude("questionScores")

        if (filter.creatorId != null) query.addCriteria(Criteria.where("creatorId").`is`(filter.creatorId))
        if (filter.name != null) query.addCriteria(Criteria.where("name").`is`(filter.name))
        if (filter.createdFrom != null) query.addCriteria(Criteria.where("created").gte(filter.createdFrom!!))
        if (filter.createdTo != null) query.addCriteria(Criteria.where("created").lte(filter.createdTo!!))
        if (filter.quizId != null ) query.addCriteria(Criteria.where("quizId").`is`(filter.quizId))

        val total = mongoTemplate.count(query, QuickQuizResult::class.java)

        return PageResult(
            elements = mongoTemplate.find(query, QuickQuizResult::class.java, "quizValidationResult"),
            total = total,
            page = pageRequest.page
        )
    }

    fun getQuickResultById(quizId: String): QuickQuizResult?{
        val query = Query(Criteria.where("_id").`is`(quizId))
        query.fields()
            .exclude("questionResults")
            .exclude("questionScores")

        return mongoTemplate.findOne(query, QuickQuizResult::class.java, "quizValidationResult")
    }
}
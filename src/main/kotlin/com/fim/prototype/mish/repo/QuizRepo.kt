package com.fim.prototype.mish.repo

import com.fim.prototype.mish.model.common.filters.FilterBase
import com.fim.prototype.mish.model.entities.quiz.QuickQuizEntity
import com.fim.prototype.mish.model.entities.quiz.QuizEntity
import com.fim.prototype.mish.repo.interfaces.IQuizRepo
import com.fim.prototype.mish.utils.PageRequestData
import com.fim.prototype.mish.utils.PageResult
import com.fim.prototype.mish.utils.createPageRequest
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

    fun getQuickQuizById(quizId: String): QuickQuizEntity? {
        val query = Query(Criteria.where("_id").`is`(quizId))

        query.fields()
            .include("chapterId")
            .include("timeLimit")
            .include("id")
            .include("name")
            .include("creatorId")
            .include("description")
            .include("created")
            .include("updated")

        return mongoTemplate.findOne(query, QuickQuizEntity::class.java, "quiz")
    }

    fun listQuizzes(pageRequest: PageRequestData, filter: FilterBase): PageResult<QuizEntity> {
        val query = Query().with(pageRequest.createPageRequest())

        if (filter.creatorId != null) query.addCriteria(Criteria.where("creatorId").`is`(filter.creatorId))
        if (filter.name != null) query.addCriteria(Criteria.where("name").`is`(filter.name))
        if (filter.createdFrom != null) query.addCriteria(Criteria.where("created").gte(filter.createdFrom!!))
        if (filter.createdTo != null) query.addCriteria(Criteria.where("created").lte(filter.createdTo!!))

        val total = mongoTemplate.count(query, QuizEntity::class.java)

        return PageResult(
            elements = mongoTemplate.find(query, QuizEntity::class.java),
            total = total,
            page = pageRequest.page
        )
    }
}
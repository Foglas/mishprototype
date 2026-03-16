package com.fim.prototype.mish.repo

import com.fim.prototype.mish.exceptions.DatabaseOperationFailedException
import com.fim.prototype.mish.model.common.filters.FilterBase
import com.fim.prototype.mish.model.entities.quiz.QuickQuizEntity
import com.fim.prototype.mish.model.entities.quiz.QuizEntity
import com.fim.prototype.mish.repo.interfaces.IQuizRepo
import com.fim.prototype.mish.utils.PageRequestData
import com.fim.prototype.mish.utils.PageResult
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service

@Service
class QuizRepo(
    private val quizRepo: IQuizRepo,
    private val mongoTemplate: MongoTemplate,
    private val mongoBaseRepoUtils: MongoBaseRepoUtils
) {

    fun save(quiz: QuizEntity): QuizEntity {
        try {
            return quizRepo.save(quiz)
        } catch (ex: Exception){
            throw DatabaseOperationFailedException("Quiz was not saved, please try again later!")
        }

    }

    fun deleteById(quizId: String){
        try {
            quizRepo.deleteById(quizId)
        } catch (ex: Exception){
            throw DatabaseOperationFailedException("Quiz was not deleted, please try again later!")
        }
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

    fun listQuizzes(pageRequest: PageRequestData, filter: FilterBase): PageResult<QuickQuizEntity> {
        val query = mongoBaseRepoUtils.createBaseFilterCriteriaAndReturnQuery(filter)
        query.fields()
            .exclude("questions")
            .exclude("answers")

        return mongoBaseRepoUtils.listPagedData(query, pageRequest, QuickQuizEntity::class, MongoCollection.QUIZ_ENTITY)

    }
}
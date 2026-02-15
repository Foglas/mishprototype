package com.fim.prototype.mish.repo

import com.fim.prototype.mish.exceptions.DatabaseOperationFailed
import com.fim.prototype.mish.model.entities.ChapterEntity
import com.fim.prototype.mish.model.entities.FullTextCollectionType
import com.fim.prototype.mish.model.entities.FullTextEntity
import com.fim.prototype.mish.model.rest.FullTextResult
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.index.TextIndexDefinition
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.TextCriteria
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository

@Repository
class FullTextRepo(
    private val mongoTemplate: MongoTemplate
) {

    fun initIndex(){
        val textIndex = TextIndexDefinition.builder()
            .onField("text")
            .withDefaultLanguage("none")
            .build()

        mongoTemplate.indexOps(FullTextEntity::class.java)
            .ensureIndex(textIndex)
    }

    fun save(fulltext: FullTextEntity): FullTextEntity {
        try {
            return mongoTemplate.save(fulltext)
        } catch (ex: Exception){
            throw DatabaseOperationFailed("Full text was not created, please try again later!")
        }
    }

    fun update(externalId: String, text: String): FullTextEntity? {
        try {
            val query = Query(Criteria.where("externalId").`is`(externalId))
            val updateQuery = Update().set("text", text)

            return mongoTemplate.findAndModify(query, updateQuery, FullTextEntity::class.java)
        } catch (ex: Exception){
            throw DatabaseOperationFailed("Full text was not updated, please try again later!")
        }
    }

    fun search(keyword: String, type: FullTextCollectionType): FullTextResult {
        val query = Query(TextCriteria.forDefaultLanguage().matching(keyword))
        query.addCriteria(Criteria.where("type").`is`(type))
        val results: List<FullTextEntity> = mongoTemplate.find(query, FullTextEntity::class.java)

        val ids = results.map { it.externalId }

        return when(type) {
            FullTextCollectionType.CHAPTER -> {
                val q = Query(Criteria.where("_id").`in`(ids))
                val chapters = mongoTemplate.find(q, ChapterEntity::class.java)
                FullTextResult(chapters)
            }
        }
    }
}
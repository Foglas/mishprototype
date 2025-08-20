package com.fim.prototype.mish.repo

import com.fim.prototype.mish.data.entities.ChapterEntity
import com.fim.prototype.mish.data.entities.FullTextCollectionType
import com.fim.prototype.mish.data.entities.FullTextEntity
import com.fim.prototype.mish.data.rest.FullTextResult
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.index.TextIndexDefinition
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.TextCriteria
import org.springframework.stereotype.Repository

@Repository
class FullTextRepo(
    private val mongoTemplate: MongoTemplate
) {

    fun initIndex(){
        val textIndex = TextIndexDefinition.builder()
            .onField("text")
            .withDefaultLanguage("none") // disables stop words
            .build()

        mongoTemplate.indexOps(FullTextEntity::class.java)
            .ensureIndex(textIndex)
    }

    fun save(fulltext: FullTextEntity): FullTextEntity {
        return mongoTemplate.save(fulltext)
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
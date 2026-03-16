package com.fim.prototype.mish.repo

import com.fim.prototype.mish.exceptions.DatabaseOperationFailedException
import com.fim.prototype.mish.model.common.FileEntityTreeWithRelated
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.GraphLookupOperation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service

@Service
class FileEntityRepo(
    private val mongoTemplate: MongoTemplate
) {

    fun loadFileTree(rootFileId: String): FileEntityTreeWithRelated? {
        val aggregation = Aggregation.newAggregation(
            Aggregation.match(Criteria.where("_id").`is`(ObjectId(rootFileId))),
            GraphLookupOperation.builder()
                .from(MongoCollection.FILE_ENTITY)
                .startWith("\$relatedFiles._id")
                .connectFrom("relatedFiles._id")
                .connectTo("_id")
                .`as`("allRelatedFiles")
        )

        return mongoTemplate.aggregate(
            aggregation,
            MongoCollection.FILE_ENTITY,
            FileEntityTreeWithRelated::class.java
        ).uniqueMappedResult
    }

    fun delete(id: String){
        try {
            mongoTemplate.remove(
                Query(Criteria.where("_id").`is`(id)),
                MongoCollection.FILE_ENTITY
            )
        } catch (ex: Exception){
            throw DatabaseOperationFailedException("File was not deleted, please try again later!")
        }
    }
}
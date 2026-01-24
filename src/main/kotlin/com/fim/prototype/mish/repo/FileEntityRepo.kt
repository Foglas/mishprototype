package com.fim.prototype.mish.repo

import com.fim.prototype.mish.model.common.FileEntityWithTree
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.GraphLookupOperation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Service

@Service
class FileEntityRepo(
    private val mongoTemplate: MongoTemplate
) {

    fun loadFileTree(rootFileId: String): FileEntityWithTree? {
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
            FileEntityWithTree::class.java
        ).uniqueMappedResult
    }
}
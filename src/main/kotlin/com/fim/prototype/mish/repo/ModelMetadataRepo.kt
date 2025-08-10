package com.fim.prototype.mish.repo

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.fim.prototype.mish.data.entities.ModelIds
import com.fim.prototype.mish.data.entities.ModelMetadataEntity
import org.bson.Document
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.ArrayOperators
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class ModelMetadataRepo(
    val mongoTemplate: MongoTemplate,
) {

    fun getModelIdsByTargetFileId(includeTextureMetadata: Boolean): List<ModelIds> {
        val pipeline = listOf(
            Document(
                "\$project", Document(
                    mapOf(
                        "_id" to 0,
                        "model" to Document(
                            mapOf(
                                "id" to Document("\$toString", "\$targetFileId"),
                                "name" to "\$name"
                            )
                        ),
                        "mainTexture" to Document(
                            "\$cond", listOf(
                                Document("\$ifNull", listOf("\$mainTexture", false)),
                                Document(
                                    mapOf(
                                        "id" to Document("\$toString", "\$mainTexture.targetFileId"),
                                        "name" to "\$mainTexture.name"
                                    )
                                ),
                                null
                            )),
                        "otherTextures" to Document(
                            "\$map", Document(
                                mapOf(
                                    "input" to "\$otherTextures",
                                    "as" to "ot",
                                    "in" to Document("id", "\$\$ot.targetFileId").append("name", "\$\$ot.name")
                                )
                            )
                        )
                    )
                )
            )
        )

        val mongoCollection = mongoTemplate.getCollection("models")
        val result = mongoCollection.aggregate(pipeline).toList()
        val objectMapper = ObjectMapper().registerKotlinModule()

        return result.map { doc ->
            println(doc.toJson())
            objectMapper.convertValue(doc, ModelIds::class.java)
        }
    }


    fun getModelMetadataEntityByTargetFileId(targetFileId: String): ModelMetadataEntity? {
        val query = Query(Criteria.where("targetFileId").`is`(targetFileId))
        return mongoTemplate.findOne(query, ModelMetadataEntity::class.java, "models")
    }

    fun save(modelMetadataEntity: ModelMetadataEntity): ModelMetadataEntity {
        return mongoTemplate.save(modelMetadataEntity, "models")
    }

}
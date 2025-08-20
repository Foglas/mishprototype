package com.fim.prototype.mish.repo

import com.fim.prototype.mish.data.entities.FileIdWithName
import com.fim.prototype.mish.data.entities.ModelIds
import com.fim.prototype.mish.data.entities.ModelMetadataEntity
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class ModelMetadataRepo(
    val mongoTemplate: MongoTemplate,
) {

    fun getAllModelMetadata(includeTextureMetadata: Boolean): List<ModelIds> {
        val query = Query()
        query.fields().include("name").include("otherTextures").include("mainTexture")
        val metadata = mongoTemplate.find(query, ModelMetadataEntity::class.java)
        return metadata.map {
            ModelIds(
                model = FileIdWithName(it.id?:"", it.name),
                mainTexture = FileIdWithName(it.mainTexture?.targetFileId?:"", it.mainTexture?.name?:""),
                otherTextures = it.otherTextures.map { FileIdWithName(it.targetFileId?:"", it.name) }
            )
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
package com.fim.prototype.mish.repo

import com.fim.prototype.mish.model.entities.FileIdWithName
import com.fim.prototype.mish.model.entities.ModelIds
import com.fim.prototype.mish.model.entities.ModelMetadataEntity
import com.fim.prototype.mish.model.rest.SimpleTextureData
import com.fim.prototype.mish.utils.PageRequestData
import com.fim.prototype.mish.utils.PageResult
import com.fim.prototype.mish.utils.createPageRequest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class ModelMetadataRepo(
    val mongoTemplate: MongoTemplate,
) {

    fun getAllModelMetadata(pageRequestData: PageRequestData): PageResult<ModelIds> {
        val query = Query().with(pageRequestData.createPageRequest())
        query.fields().include("name").include("targetFileId").include("otherTextures").include("mainTexture")
        val metadata = mongoTemplate.find(query, ModelMetadataEntity::class.java)
        val total = mongoTemplate.count(Query(), ModelMetadataEntity::class.java)

        val elements = metadata.map {
            ModelIds(
                metadataId = it.id?:"",
                model = FileIdWithName(it.targetFileId?:"", it.name),
                mainTexture = it.mainTexture?.let { SimpleTextureData(it.targetFileId?:"", it.name, it.csvContent) },
                otherTextures = it.otherTextures.map { SimpleTextureData(it.targetFileId?:"", it.name, it.csvContent) }
            )
        }

        return PageResult(
            elements = elements,
            total = total,
            page = pageRequestData.page
        )
    }


    fun getModelMetadataEntityByTargetFileId(targetFileId: String): ModelMetadataEntity? {
        val query = Query(Criteria.where("targetFileId").`is`(targetFileId))
        return mongoTemplate.findOne(query, ModelMetadataEntity::class.java, "models")
    }

    fun save(modelMetadataEntity: ModelMetadataEntity): ModelMetadataEntity {
        return mongoTemplate.save(modelMetadataEntity, "models")
    }

}
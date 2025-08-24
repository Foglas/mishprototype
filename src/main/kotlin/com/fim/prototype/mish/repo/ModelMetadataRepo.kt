package com.fim.prototype.mish.repo

import com.fim.prototype.mish.data.entities.FileIdWithName
import com.fim.prototype.mish.data.entities.ModelIds
import com.fim.prototype.mish.data.entities.ModelMetadataEntity
import com.fim.prototype.mish.data.rest.SimpleTextureData
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
        query.fields().include("name").include("otherTextures").include("mainTexture")
        val metadata = mongoTemplate.find(query, ModelMetadataEntity::class.java)
        val total = mongoTemplate.count(Query(), ModelMetadataEntity::class.java)

        val elements = metadata.map {
            ModelIds(
                model = FileIdWithName(it.id?:"", it.name),
                mainTexture = SimpleTextureData(it.mainTexture?.targetFileId?:"", it.mainTexture?.name?:"", it.mainTexture?.csvContent?:""),
                otherTextures = it.otherTextures.map { SimpleTextureData(it.targetFileId?:"", it.name, it.csvContent?:"") }
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
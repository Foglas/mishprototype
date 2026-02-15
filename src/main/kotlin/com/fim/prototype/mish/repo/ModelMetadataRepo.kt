package com.fim.prototype.mish.repo

import com.fim.prototype.mish.exceptions.NotFoundException
import com.fim.prototype.mish.model.entities.FileIdWithName
import com.fim.prototype.mish.model.entities.FileSenseType
import com.fim.prototype.mish.model.entities.ModelIds
import com.fim.prototype.mish.model.entities.ModelMetadataEntity
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
        query.fields().include("name").include("modelId").include("relatedFiles").include("isAdvanced")
        val metadata = mongoTemplate.find(query, ModelMetadataEntity::class.java)
        val total = mongoTemplate.count(Query(), ModelMetadataEntity::class.java)

        val elements = metadata.map {
            ModelIds(
                metadataId = it.id?:"",
                model = FileIdWithName(it.modelId?:"", it.name, FileSenseType.MODEL, related = it.relatedFiles.map { FileIdWithName(it.id, it.name, it.senseType) }),
            )
        }

        return PageResult(
            elements = elements,
            total = total,
            page = pageRequestData.page
        )
    }

    fun deleteMetadataById(id: String){
        val query = Query(Criteria.where("_id").`is`(id))
        mongoTemplate.remove(query, ModelMetadataEntity::class.java, MongoCollection.MODEL_ENTITY)
    }

    fun getModelMetadataEntityByTargetFileId(targetFileId: String): ModelMetadataEntity? {
        return getModelMetadataBy("targetFileId", targetFileId)
    }

    fun getModelMetadataEntityByTextureFileId(targetFileId: String): ModelMetadataEntity? {
        val query = Query(
            Criteria().orOperator(
                Criteria.where("mainTexture.targetFileId").`is`(targetFileId),
                Criteria.where("otherTextures.targetFileId").`is`(targetFileId)
            )
        )
        return mongoTemplate.findOne(query, ModelMetadataEntity::class.java, MongoCollection.MODEL_ENTITY)
    }

    fun getModelMetadataById(id: String): ModelMetadataEntity{
       return getModelMetadataBy("_id", id) ?: throw NotFoundException("Model metadata was not found!")
    }

    fun getModelMetadataBy(property: String, value: String): ModelMetadataEntity?{
        val query = Query(Criteria.where(property).`is`(value))
        return mongoTemplate.findOne(query, ModelMetadataEntity::class.java, MongoCollection.MODEL_ENTITY)
    }

    fun save(modelMetadataEntity: ModelMetadataEntity): ModelMetadataEntity {
        return mongoTemplate.save(modelMetadataEntity, MongoCollection.MODEL_ENTITY)
    }

}
package com.fim.prototype.mish.repo

import com.fim.prototype.mish.model.common.FileEntityWithTree
import com.fim.prototype.mish.model.entities.*
import com.fim.prototype.mish.utils.PageRequestData
import com.fim.prototype.mish.utils.PageResult
import com.fim.prototype.mish.utils.createPageRequest
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.GraphLookupOperation
import org.springframework.data.mongodb.core.query.Collation
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

    fun deleteMetadataByTargetFileId(targetFileId: String){
        val query = Query(Criteria.where("targetFileId").`is`(targetFileId))
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

    fun getModelMetadataBy(property: String, value: String): ModelMetadataEntity?{
        val query = Query(Criteria.where(property).`is`(value))
        return mongoTemplate.findOne(query, ModelMetadataEntity::class.java, MongoCollection.MODEL_ENTITY)
    }

    fun save(modelMetadataEntity: ModelMetadataEntity): ModelMetadataEntity {
        return mongoTemplate.save(modelMetadataEntity, MongoCollection.MODEL_ENTITY)
    }

}
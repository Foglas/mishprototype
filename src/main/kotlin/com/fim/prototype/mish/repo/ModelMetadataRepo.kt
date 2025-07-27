package com.fim.prototype.mish.repo

import com.fim.prototype.mish.data.models.entities.ModelMetadataEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ModelMetadataRepo : MongoRepository<ModelMetadataEntity, String> {

   fun getModelMetadataEntityByTargetFileId(targetFileId: String): ModelMetadataEntity?

   fun save(modelMetadataEntity: ModelMetadataEntity)
}
package com.fim.prototype.mish.services

import com.fim.prototype.mish.data.TextureUpload
import com.fim.prototype.mish.data.entities.FileIdWithName
import com.fim.prototype.mish.data.entities.ModelIds
import com.fim.prototype.mish.data.models.entities.ModelMetadataEntity
import com.fim.prototype.mish.exceptions.NotFoundException
import com.fim.prototype.mish.repo.BasicFileStorageRepo
import com.fim.prototype.mish.repo.ModelMetadataRepo
import org.bson.types.ObjectId
import org.springframework.data.mongodb.gridfs.GridFsResource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class ModelService(
    private val basicFileStorageRepo: BasicFileStorageRepo,
    private val modelMetadataRepo: ModelMetadataRepo
) {

    fun uploadModel(model: MultipartFile, metadata: ModelMetadataEntity): ModelIds {
        val objectId = basicFileStorageRepo.uploadFile(model)
        metadata.targetFileId = objectId.toHexString()
        modelMetadataRepo.save(metadata)
        return ModelIds(FileIdWithName(objectId.toHexString(), metadata.name))
    }

    @Transactional
    fun uploadTexture(texture: MultipartFile, metadata: TextureUpload): ObjectId {
        val modelMetadata = modelMetadataRepo.getModelMetadataEntityByTargetFileId(metadata.modelId) ?: throw NotFoundException("Model metadata was not found!")

        val objectId = basicFileStorageRepo.uploadFile(texture)

        if (metadata.isPrimary) {
            modelMetadata.mainTexture = metadata.texture
        } else {
            modelMetadata.otherTextures += metadata.texture
        }
        metadata.texture.targetFileId = objectId.toHexString()

        modelMetadataRepo.save(modelMetadata)
        return basicFileStorageRepo.uploadFile(texture)
    }

    fun getFileById(itemId: String): GridFsResource{
        return basicFileStorageRepo.getFileById(itemId) ?: throw NotFoundException("File with id $itemId not found!")
    }

}
package com.fim.prototype.mish.services.chapters

import com.fim.prototype.mish.exceptions.NotFoundException
import com.fim.prototype.mish.model.entities.FileIdWithName
import com.fim.prototype.mish.model.entities.ModelIds
import com.fim.prototype.mish.model.entities.ModelMetadataEntity
import com.fim.prototype.mish.model.entities.QuickCommonFileEntity
import com.fim.prototype.mish.model.rest.SimpleTextureData
import com.fim.prototype.mish.model.rest.TextureUpload
import com.fim.prototype.mish.repo.BasicFileStorageRepo
import com.fim.prototype.mish.repo.ModelMetadataRepo
import com.fim.prototype.mish.utils.PageRequestData
import com.fim.prototype.mish.utils.PageResult
import org.springframework.data.mongodb.gridfs.GridFsResource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class ModelService(
    private val basicFileStorageRepo: BasicFileStorageRepo,
    private val modelMetadataRepo: ModelMetadataRepo,
) {

    fun uploadModel(model: MultipartFile, modelName: String, relatedFiles: List<MultipartFile>, relatedFilesMetadata: List<QuickCommonFileEntity>): ModelIds {
        val objectId = basicFileStorageRepo.uploadFile(model)

        //TODO merge relatedFiles with QuickCommonEntity by fileName
        

        metadata.targetFileId = objectId.toHexString()
        val metadataId = modelMetadataRepo.save(metadata).id
        return ModelIds(metadataId?:"", FileIdWithName(objectId.toHexString(), metadata.name))
    }

    @Transactional
    fun uploadTexture(texture: MultipartFile, metadata: TextureUpload): SimpleTextureData {
        val modelMetadata = modelMetadataRepo.getModelMetadataEntityByTargetFileId(metadata.modelId) ?: throw NotFoundException("Model metadata was not found!")

        val objectId = basicFileStorageRepo.uploadFile(texture)

        metadata.texture.targetFileId = objectId.toHexString()

        if (metadata.isPrimary) {
            modelMetadata.mainTexture = metadata.texture
        } else {
            modelMetadata.otherTextures += metadata.texture
        }


        modelMetadataRepo.save(modelMetadata)
        return SimpleTextureData(objectId.toHexString(), metadata.texture.name, metadata.texture.csvContent)
    }

    fun deleteModel(modelId: String){
        modelMetadataRepo.deleteMetadataByTargetFileId(modelId)
        basicFileStorageRepo.deleteFile(modelId)
    }

    fun deleteTexture(textureId: String){
        val modelMetadata = modelMetadataRepo.getModelMetadataEntityByTextureFileId(textureId)?: return

        val updated = modelMetadata.copy(
            mainTexture = if (modelMetadata.mainTexture?.targetFileId == textureId) null else modelMetadata.mainTexture,
            otherTextures = modelMetadata.otherTextures.filter { it.targetFileId != textureId }.toMutableList()
        )

        modelMetadataRepo.save(updated)
    }

    fun listModelMetadata(pageRequestData: PageRequestData): PageResult<ModelIds> {
        return modelMetadataRepo.getAllModelMetadata(pageRequestData)

    }

    fun isFileExists(itemId: String): Boolean {
        return basicFileStorageRepo.isFileExists(itemId)
    }

    fun getFileById(itemId: String): GridFsResource{
        return basicFileStorageRepo.getFileById(itemId) ?: throw NotFoundException("File with id $itemId not found!")
    }
}
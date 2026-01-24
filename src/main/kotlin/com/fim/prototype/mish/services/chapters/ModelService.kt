package com.fim.prototype.mish.services.chapters

import com.fim.prototype.mish.exceptions.ValidationException
import com.fim.prototype.mish.model.common.FileEntityWithTree
import com.fim.prototype.mish.model.entities.*
import com.fim.prototype.mish.repo.ModelMetadataRepo
import com.fim.prototype.mish.services.FileService
import com.fim.prototype.mish.utils.PageRequestData
import com.fim.prototype.mish.utils.PageResult
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ModelService(
    private val modelMetadataRepo: ModelMetadataRepo,
    private val fileService: FileService,
) {
    //TODO logic about deletingFiles etc. should be moved to FileService and than only called

    suspend fun uploadModel(
        files: List<MultipartFile>,
        metadata: InputFileDesc
    ): ModelIds {
        val groupedRelatedFiles = files.associateBy { it.originalFilename ?: "" }

        if (metadata.fileSenseType != FileSenseType.MODEL) throw ValidationException("Files are not related to parent model!")

        val relatedFilesMetadata = fileService.uploadFilesRecursively(groupedRelatedFiles, metadata)

        val info = ModelMetadataEntity.from(relatedFilesMetadata.toFileEntity())

        val metadataModel = modelMetadataRepo.save(info)

        return ModelIds(metadataModel.id ?: "", FileIdWithName(relatedFilesMetadata.id ?: "", metadataModel.name, FileSenseType.MODEL, mapRelatedFiles(relatedFilesMetadata.relatedFiles)))
    }

    fun getModelRelatedTree(modelMetadataId: String): FileEntityWithTree {
       val modelMetadata = getModelMetadataById(modelMetadataId)
       return fileService.loadFileTree(modelMetadata.modelId)
    }

    fun getModelMetadataById(id: String): ModelMetadataEntity {
       return modelMetadataRepo.getModelMetadataById(id)
    }

    fun deleteModel(modelMetadataId: String, force: Boolean = false) {
        val modelMetadata = getModelMetadataById(modelMetadataId)
        fileService.deleteFile(modelMetadata.modelId)

        //TODO implement force delete
    }

    //load all related
    fun listModelMetadata(pageRequestData: PageRequestData): PageResult<ModelIds> {
        return modelMetadataRepo.getAllModelMetadata(pageRequestData)
    }

    private fun mapRelatedFiles(relatedFiles: List<OutputFileEntity>): List<FileIdWithName>{
        if (relatedFiles.isEmpty()) return emptyList()
        return relatedFiles.map { FileIdWithName(it.id?:"", it.name, it.senseType, mapRelatedFiles(it.relatedFiles)) }
    }
}
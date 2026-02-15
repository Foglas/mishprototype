package com.fim.prototype.mish.services.chapters

import com.fim.prototype.mish.exceptions.ValidationException
import com.fim.prototype.mish.model.common.FileEntityRecursive
import com.fim.prototype.mish.model.common.FileEntityTree
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

    fun getModelRelatedTree(modelMetadataId: String): FileEntityTree {
       val modelMetadata = getModelMetadataById(modelMetadataId)
       val fileTree = fileService.loadFileTree(modelMetadata.modelId)

       return FileEntityTree(fileTree.id, fileTree.name, fileTree.creatorId, fileTree.description, fileTree.contentType, fileTree.size, fileTree.senseType, fileTree.backendEndpoint, fileTree.created, fileTree.updated, allRelatedFiles = createFileTree(fileTree.relatedFiles, fileTree.allRelatedFiles))
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

    private fun createFileTree(rootFiles: List<FileIdentifier>, allRelatedFiles: List<FileEntity>): List<FileEntityRecursive> {
        val allFiles = allRelatedFiles.groupBy { it.id }.map { it.key to it.value.first() }.toMap()

        return rootFiles.mapNotNull { allFiles[it.id] }.map { file ->
            FileEntityRecursive(file.id, file.name, file.creatorId, file.description, file.contentType, file.size, file.senseType, file.backendEndpoint, file.created, file.updated, relatedFiles = createFileTreeRecursive(file, allFiles))
        }
    }

    private fun createFileTreeRecursive(file: FileEntity, allRelatedFiles: Map<String?, FileEntity>): List<FileEntityRecursive> {
        if (file.relatedFiles.isEmpty()) return emptyList()

        val relatedFiles = file.relatedFiles
        val allRelatedFilesEntity = relatedFiles.mapNotNull { allRelatedFiles[it.id] }

        return allRelatedFilesEntity.map {
            FileEntityRecursive(it.id, it.name, it.creatorId, it.description, it.contentType, it.size, it.senseType, it.backendEndpoint, it.created, it.updated, relatedFiles = createFileTreeRecursive(it, allRelatedFiles))
        }
    }
}
package com.fim.prototype.mish.services.chapters

import com.fim.prototype.mish.exceptions.NotFoundException
import com.fim.prototype.mish.exceptions.ValidationException
import com.fim.prototype.mish.model.entities.*
import com.fim.prototype.mish.model.rest.SimpleTextureData
import com.fim.prototype.mish.model.rest.TextureUpload
import com.fim.prototype.mish.repo.BasicFileStorageRepo
import com.fim.prototype.mish.repo.ModelMetadataRepo
import com.fim.prototype.mish.repo.interfaces.IFileRepo
import com.fim.prototype.mish.utils.PageRequestData
import com.fim.prototype.mish.utils.PageResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.springframework.data.mongodb.gridfs.GridFsResource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class ModelService(
    private val basicFileStorageRepo: BasicFileStorageRepo,
    private val modelMetadataRepo: ModelMetadataRepo,
    private val fileRepo: IFileRepo,
) {

    private val scope = CoroutineScope(Dispatchers.IO)

    suspend fun uploadModel(
        files: List<MultipartFile>,
        metadata: InputFileDesc
    ): ModelIds {
        val groupedRelatedFiles = files.associateBy { it.originalFilename ?: "" }

        //Match metadataWithCorrectFile.. group files by name and then find in that map by originalFileName
        //for each over metadata and every inner metadata
        //save all files and return objectIDs.. objectIds set into metadata and return List<Metadata>.. also for inner relatedFiles

        if (metadata.fileSenseType != FileSenseType.MODEL) throw ValidationException("Files are not related to parent model!")

        val relatedFilesMetadata = uploadFiles(groupedRelatedFiles, metadata)

        val info = ModelMetadataEntity.from(relatedFilesMetadata.toFileEntity())

        val metadataModel = modelMetadataRepo.save(info)

        return ModelIds(metadataModel.id ?: "", FileIdWithName(metadataModel.id ?: "", metadataModel.name, FileSenseType.MODEL, mapRelatedFiles(relatedFilesMetadata.relatedFiles)))
    }


    private fun mapRelatedFiles(relatedFiles: List<OutputFileEntity>): List<FileIdWithName>{
        if (relatedFiles.isEmpty()) return emptyList()
        return relatedFiles.map { FileIdWithName(it.id?:"", it.name, it.senseType, mapRelatedFiles(it.relatedFiles)) }
    }

    //TODO move upload outside of method - run in coroutines and than input should be Map<String, String> eg: Map<originalFileName, ObjectId>
    private fun uploadFiles(
        files: Map<String, MultipartFile>,
        metadata: InputFileDesc,
        visited: MutableMap<String, String> = mutableMapOf()
    ): OutputFileEntity {

        val file = files[metadata.originalFileName]
            ?: throw ValidationException("File ${metadata.originalFileName} not found")

        val alreadySaved = visited[metadata.originalFileName]

        val objectId = if (alreadySaved != null) {
            alreadySaved
        } else {
            val objectId = basicFileStorageRepo.uploadFile(file).toHexString()
            visited[metadata.originalFileName] = objectId
            objectId
        }

        val fileEntity = file.getOutputFileEntity(
            metadata.copy(id = objectId),
            metadata.relatedFiles.map {
                uploadFiles(files, it, visited)
            }
        )

        if (alreadySaved == null) fileRepo.save(fileEntity.toFileEntity())
        return fileEntity
    }


    @Transactional
    fun uploadTexture(texture: MultipartFile, metadata: TextureUpload): SimpleTextureData {
        val modelMetadata = modelMetadataRepo.getModelMetadataEntityByTargetFileId(metadata.modelId)
            ?: throw NotFoundException("Model metadata was not found!")

        val objectId = basicFileStorageRepo.uploadFile(texture)

        metadata.texture.id = objectId.toHexString()

        val updated =
            modelMetadata.copy(relatedFiles = modelMetadata.relatedFiles.toMutableList() + FileIdentifier(metadata.texture.id?:"", metadata.texture.name, metadata.texture.senseType))

        modelMetadataRepo.save(updated)
        return SimpleTextureData(objectId.toHexString(), metadata.texture.name)
    }

    fun deleteModel(modelId: String) {
        modelMetadataRepo.deleteMetadataByTargetFileId(modelId)
        basicFileStorageRepo.deleteFile(modelId)
    }

    fun deleteTexture(textureId: String) {
        val modelMetadata = modelMetadataRepo.getModelMetadataEntityByTextureFileId(textureId) ?: return

        val updated = modelMetadata.copy(relatedFiles = modelMetadata.relatedFiles.filter { it.id != textureId })
        modelMetadataRepo.save(updated)
    }

    fun listModelMetadata(pageRequestData: PageRequestData): PageResult<ModelIds> {
        return modelMetadataRepo.getAllModelMetadata(pageRequestData)

    }

    fun isFileExists(itemId: String): Boolean {
        return basicFileStorageRepo.isFileExists(itemId)
    }

    fun getFileById(itemId: String): GridFsResource {
        return basicFileStorageRepo.getFileById(itemId) ?: throw NotFoundException("File with id $itemId not found!")
    }
}
package com.fim.prototype.mish.services

import com.fim.prototype.mish.exceptions.NotFoundException
import com.fim.prototype.mish.exceptions.ValidationException
import com.fim.prototype.mish.model.common.FileEntityTreeWithRelated
import com.fim.prototype.mish.model.entities.InputFileDesc
import com.fim.prototype.mish.model.entities.OutputFileEntity
import com.fim.prototype.mish.model.entities.getOutputFileEntity
import com.fim.prototype.mish.model.entities.toFileEntity
import com.fim.prototype.mish.repo.BasicFileStorageRepo
import com.fim.prototype.mish.repo.FileEntityRepo
import com.fim.prototype.mish.repo.interfaces.IFileRepo
import org.springframework.data.mongodb.gridfs.GridFsResource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class FileService(
    private val basicFileStorageRepo: BasicFileStorageRepo,
    private val fileRepo: IFileRepo,
    private val fileEntityRepo: FileEntityRepo
    ) {

    //TODO move upload outside of method - run in coroutines and than input should be Map<String, String> eg: Map<originalFileName, ObjectId>
    fun uploadFilesRecursively(
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
                uploadFilesRecursively(files, it, visited)
            }
        )

        fileRepo.save(fileEntity.toFileEntity())
        return fileEntity
    }

    //TODO needs to be completely refactored - now it should assign file into the related of some other
    @Transactional
    fun assignRelatedFile(parentFileMetadataId: String){
        //TODO not implemented
    }

    fun loadFileTree(rootFileId: String): FileEntityTreeWithRelated{
        return fileEntityRepo.loadFileTree(rootFileId) ?: throw NotFoundException("Root file was not found!")
    }

    fun deleteFile(id: String){
        basicFileStorageRepo.deleteFile(id)
    }

    fun isFileExists(itemId: String): Boolean {
        return basicFileStorageRepo.isFileExists(itemId)
    }

    fun getFileById(itemId: String): GridFsResource {
        return basicFileStorageRepo.getFileById(itemId) ?: throw NotFoundException("File with id $itemId not found!")
    }
}
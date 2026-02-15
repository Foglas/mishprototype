package com.fim.prototype.mish.services

import com.fim.prototype.mish.exceptions.NotFoundException
import com.fim.prototype.mish.exceptions.ValidationException
import com.fim.prototype.mish.model.common.FileEntityRecursive
import com.fim.prototype.mish.model.common.FileEntityTree
import com.fim.prototype.mish.model.common.FileEntityTreeWithRelated
import com.fim.prototype.mish.model.entities.*
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

    fun loadFileTree(rootFileId: String): FileEntityTree{
        val fileTree = loafFileTreeFlatted(rootFileId)
        return FileEntityTree(fileTree.id, fileTree.name, fileTree.creatorId, fileTree.description, fileTree.contentType, fileTree.size, fileTree.senseType, fileTree.backendEndpoint, fileTree.created, fileTree.updated, allRelatedFiles = createFileTree(fileTree.relatedFiles, fileTree.allRelatedFiles))
    }

    fun loafFileTreeFlatted(rootFileId: String): FileEntityTreeWithRelated {
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
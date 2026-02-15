package com.fim.prototype.mish.repo

import com.fim.prototype.mish.exceptions.DatabaseOperationFailed
import com.fim.prototype.mish.exceptions.NotFoundException
import com.mongodb.client.gridfs.model.GridFSFile
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsResource
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.stereotype.Repository
import org.springframework.web.multipart.MultipartFile

@Repository
class BasicFileStorageRepo(
    private val gridFs: GridFsTemplate
) {

    fun uploadFile(model: MultipartFile): ObjectId {
        try {
            return gridFs.store(model.inputStream, model.originalFilename, model.contentType)
        } catch (ex: Exception) {
            throw DatabaseOperationFailed("File was not uploaded, please try again later!")
        }
    }

    fun getFileById(objectId: String): GridFsResource? {
        val file = gridFs.findOne(Query(Criteria.where("_id").`is`(ObjectId(objectId)))) as GridFSFile?
        return file?.let { gridFs.getResource(it) }
    }

    fun deleteFile(objectId: String) {
        try {
            ObjectId(objectId)
            val query = Query(Criteria.where("_id").`is`(ObjectId(objectId)))
            gridFs.delete(query)
        } catch (e: IllegalArgumentException) {
            throw DatabaseOperationFailed("File was not deleted, please try again later!")
        }


    }

    fun isFileExists(objectId: String): Boolean {
        try {
            val query = Query(Criteria.where("_id").`is`(ObjectId(objectId))).limit(1)
            return gridFs.find(query).any()
        } catch (ex: Exception) {
            throw NotFoundException("File with id $objectId was not found")
        }
    }

}
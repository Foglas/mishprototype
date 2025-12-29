package com.fim.prototype.mish.repo

import com.fim.prototype.mish.exceptions.NotFoundException
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
        return gridFs.store(model.inputStream, model.originalFilename, model.contentType)
    }

    fun getFileById(objectId: String): GridFsResource? {
        val file = gridFs.findOne(Query(Criteria.where("_id").`is`(ObjectId(objectId))))
        return gridFs.getResource(file)
    }

    fun deleteFile(objectId: String) {
        val id = try {
            ObjectId(objectId)
        } catch (e: IllegalArgumentException) {
            throw NotFoundException("File with id $objectId was not found")
        }

        val query = Query(Criteria.where("_id").`is`(id))
        gridFs.delete(query)
    }

    fun isFileExists(objectId: String): Boolean {
        try {
            val query = Query(Criteria.where("_id").`is`(ObjectId(objectId))).limit(1)
            return gridFs.find(query).any()
        } catch (ex: Exception){
            throw NotFoundException("File with id $objectId was not found")
        }
    }

}
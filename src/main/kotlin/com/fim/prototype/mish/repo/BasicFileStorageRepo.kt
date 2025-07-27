package com.fim.prototype.mish.repo

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

}
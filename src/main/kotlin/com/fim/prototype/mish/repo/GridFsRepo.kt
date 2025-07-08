package com.fim.prototype.mish.repo

import com.fim.prototype.mish.data.MetadataTypes
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsResource
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.stereotype.Repository
import org.springframework.web.multipart.MultipartFile

@Repository
class GridFsRepo(
    private val gridFs: GridFsTemplate
) {

    fun uploadFile(model: MultipartFile, metadata: MutableMap<MetadataTypes, String> = mutableMapOf()): ObjectId {
        return gridFs.store(model.inputStream, model.originalFilename, metadata)
    }

    fun getFileByMetadata(property: MutableMap<MetadataTypes, String>): GridFsResource?{
        //TODO implement multi property searching
        val model = gridFs.findOne(Query(Criteria.where("metadata.${MetadataTypes.TARGET_ID}").`is`(property[MetadataTypes.TARGET_ID])))
        return gridFs.getResource(model)
    }

    fun getFileById(objectId: String): GridFsResource? {
        val file = gridFs.findOne(Query(Criteria.where("_id").`is`(ObjectId(objectId))))
        return gridFs.getResource(file)
    }

}
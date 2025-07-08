package com.fim.prototype.mish.services

import com.fim.prototype.mish.data.MetadataTypes
import com.fim.prototype.mish.repo.GridFsRepo
import org.bson.types.ObjectId
import org.springframework.data.mongodb.gridfs.GridFsResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ModelService(
    private val gridFsRepo: GridFsRepo
) {

    fun uploadModel(model: MultipartFile, itemId: String): ObjectId {
        return gridFsRepo.uploadFile(model, mutableMapOf(MetadataTypes.TARGET_ID to itemId))
    }

    fun getModel(itemId: String): GridFsResource?{
        return gridFsRepo.getFileByMetadata(mutableMapOf(MetadataTypes.TARGET_ID to itemId))
    }

}
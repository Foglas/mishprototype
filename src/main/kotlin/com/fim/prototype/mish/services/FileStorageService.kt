package com.fim.prototype.mish.services

import com.fim.prototype.mish.repo.GridFsRepo
import org.bson.types.ObjectId
import org.springframework.data.mongodb.gridfs.GridFsResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class FileStorageService(
    private val gridFsRepo: GridFsRepo
) {

    fun uploadFile(model: MultipartFile): ObjectId {
        return gridFsRepo.uploadFile(model)
    }

    fun getFileById(itemId: String): GridFsResource?{
        return gridFsRepo.getFileById(itemId)
    }

}
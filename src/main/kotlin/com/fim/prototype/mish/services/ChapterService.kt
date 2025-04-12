package com.fim.prototype.mish.services

import com.fim.prototype.mish.data.ChapterEntity
import com.fim.prototype.mish.repo.ChapterRepo
import com.mongodb.client.gridfs.model.GridFSFile
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsResource
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile


@Service
class ChapterService(
    private val chapterRepo: ChapterRepo,
    private val gridFs: GridFsTemplate
) {

    fun createChapter(chapter: ChapterEntity): ChapterEntity {
        return chapterRepo.save(chapter)
    }

    fun getChapter(chapterId: String): ChapterEntity? {
        return chapterRepo.findById(chapterId).orElse(null)
    }

    fun uploadModel(model: MultipartFile, chapterId: String): ObjectId {
        val metadata = mutableMapOf("chapterId" to chapterId)
        return gridFs.store(model.inputStream, model.originalFilename, metadata)
    }

    fun getModel(chapterId: String): GridFsResource?{
        val model = gridFs.findOne(Query(Criteria.where("metadata.chapterId").`is`(chapterId)))
        return gridFs.getResource(model)
    }
}


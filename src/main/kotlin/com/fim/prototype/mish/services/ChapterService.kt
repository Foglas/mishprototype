package com.fim.prototype.mish.services

import com.fim.prototype.mish.data.entities.ChapterEntity
import com.fim.prototype.mish.exceptions.ValidationException
import com.fim.prototype.mish.repo.ChapterRepo
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.TextCriteria
import org.springframework.stereotype.Service


@Service
class ChapterService(
    private val chapterRepo: ChapterRepo,
    private val mongoTemplate: MongoTemplate
) {
    fun createChapter(chapter: ChapterEntity): ChapterEntity {
        if (chapter.name.isBlank()) throw ValidationException("Chapter name should be set!", chapter)
        if (chapter.content.isBlank()) throw ValidationException("Chapter content should be set!", chapter)
        return chapterRepo.save(chapter)
    }

    fun getChapter(chapterId: String): ChapterEntity {
        return chapterRepo.findById(chapterId).orElse(null) ?: throw ValidationException("Chapter with id $chapterId is not found!")
    }

    fun searchFullText(keyword: String): List<ChapterEntity> {
        val query = Query()
        query.addCriteria(TextCriteria.forDefaultLanguage().matching(keyword))  // $text query
        return mongoTemplate.find(query, ChapterEntity::class.java)
    }
}


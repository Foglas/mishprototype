package com.fim.prototype.mish.services

import com.fim.prototype.mish.data.entities.ChapterEntity
import com.fim.prototype.mish.data.entities.FullTextCollectionType
import com.fim.prototype.mish.data.entities.FullTextEntity
import com.fim.prototype.mish.data.rest.FullTextResult
import com.fim.prototype.mish.exceptions.ValidationException
import com.fim.prototype.mish.repo.ChapterRepo
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Service


@Service
class ChapterService(
    private val chapterRepo: ChapterRepo,
    private val fullTextSearchingService: FullTextSearchingService,
) {
    fun createChapter(chapter: ChapterEntity): ChapterEntity {
        if (chapter.name.isBlank()) throw ValidationException("Chapter name should be set!", chapter)
        if (chapter.content.isBlank()) throw ValidationException("Chapter content should be set!", chapter)
        val createdChapter = chapterRepo.save(chapter)
        fullTextSearchingService.saveFullTextEntity(createdChapter.id!!, FullTextCollectionType.CHAPTER, createdChapter.name, createdChapter.content)
        return chapterRepo.save(chapter)
    }

    fun getChapter(chapterId: String): ChapterEntity {
        return chapterRepo.findById(chapterId).orElse(null) ?: throw ValidationException("Chapter with id $chapterId is not found!")
    }

    fun searchFullText(keyword: String): FullTextResult {
    return fullTextSearchingService.search(keyword, FullTextCollectionType.CHAPTER)
    }
}


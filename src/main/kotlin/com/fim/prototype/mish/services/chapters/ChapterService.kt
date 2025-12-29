package com.fim.prototype.mish.services.chapters

import com.fim.prototype.mish.model.entities.ChapterEntity
import com.fim.prototype.mish.model.entities.FullTextCollectionType
import com.fim.prototype.mish.model.rest.FullTextResult
import com.fim.prototype.mish.exceptions.ForbiddenActionException
import com.fim.prototype.mish.exceptions.NotFoundException
import com.fim.prototype.mish.exceptions.ValidationException
import com.fim.prototype.mish.repo.interfaces.IChapterRepo
import com.fim.prototype.mish.security.service.CurrentUserService
import com.fim.prototype.mish.services.fulltext.FullTextSearchingService
import com.fim.prototype.mish.utils.PageRequestData
import com.fim.prototype.mish.utils.PageResult
import com.fim.prototype.mish.utils.createPageRequest
import com.fim.prototype.mish.utils.toPageResult
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service


@Service
class ChapterService(
    private val chapterRepo: IChapterRepo,
    private val fullTextSearchingService: FullTextSearchingService,
    private val currentUserService: CurrentUserService
) {
    fun createChapter(chapter: ChapterEntity): ChapterEntity {
        validateChapter(chapter)
        val createdChapter = chapterRepo.save(chapter)
        fullTextSearchingService.saveFullTextEntity(createdChapter.id!!, FullTextCollectionType.CHAPTER, createdChapter.name, createdChapter.content)
        return createdChapter
    }

    fun updateChapter(chapter: ChapterEntity): ChapterEntity {
        val existedChapter = chapter.id?.let { getChapterById(it) } ?: throw ValidationException("Chapter id is not set!", chapter)
        if (existedChapter.creatorId == chapter.creatorId) throw ForbiddenActionException("Chapter creator id can't be changed!", chapter)
        if ((existedChapter.content != chapter.content && chapter.content.isNotBlank()) || (existedChapter.name != chapter.name && chapter.name.isNotBlank())){
            fullTextSearchingService.updateFullTextEntity(existedChapter.id!!, existedChapter.name, existedChapter.content)
        }

        return chapterRepo.save(existedChapter)
    }

    fun getChapterById(id: String): ChapterEntity {
        return chapterRepo.findByIdOrNull(id) ?: throw NotFoundException("Chapter with id $id not found")
    }

    fun getAllChapters(page: PageRequestData): PageResult<ChapterEntity> {
        return chapterRepo.findAll(page.createPageRequest()).toPageResult()
    }

    fun searchFullText(keyword: String): FullTextResult {
    return fullTextSearchingService.search(keyword, FullTextCollectionType.CHAPTER)
    }

    private fun validateChapter(chapter: ChapterEntity): ChapterEntity {
        chapter.creatorId = currentUserService.getCurrentUser().userId

        if (chapter.name.isBlank()) throw ValidationException("Chapter name should be set!", chapter)
        if (chapter.content.isBlank()) throw ValidationException("Chapter content should be set!", chapter)

        return chapter
    }
}


package com.fim.prototype.mish.repo

import com.fim.prototype.mish.exceptions.DatabaseOperationFailedException
import com.fim.prototype.mish.model.common.filters.FilterBase
import com.fim.prototype.mish.model.entities.ChapterEntity
import com.fim.prototype.mish.repo.interfaces.IChapterRepo
import com.fim.prototype.mish.utils.PageRequestData
import com.fim.prototype.mish.utils.PageResult
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ChapterRepo(
    private val chapterRepo: IChapterRepo,
    private val mongoBaseRepoUtils: MongoBaseRepoUtils
) {

    fun save(chapter: ChapterEntity): ChapterEntity {
        try {
            return chapterRepo.save(chapter)
        } catch (ex: Exception) {
            throw DatabaseOperationFailedException("Chapter was not created, please try again later!")
        }
    }

    fun getById(chapterId: String): ChapterEntity? {
        return chapterRepo.findByIdOrNull(chapterId)
    }

    fun delete(chapterId: String) {
        try {
            return chapterRepo.deleteById(chapterId)
        } catch (ex: Exception) {
            throw DatabaseOperationFailedException("Chapter was not deleted, please try again later!")
        }
    }

    fun listChapters(pageRequest: PageRequestData, filter: FilterBase): PageResult<ChapterEntity> {
        return mongoBaseRepoUtils.listPagedData(
            mongoBaseRepoUtils.createBaseFilterCriteriaAndReturnQuery(filter),
            pageRequest,
            ChapterEntity::class
        )
    }


}
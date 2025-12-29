package com.fim.prototype.mish.repo

import com.fim.prototype.mish.model.common.filters.FilterBase
import com.fim.prototype.mish.model.entities.ChapterEntity
import com.fim.prototype.mish.repo.interfaces.IChapterRepo
import com.fim.prototype.mish.utils.PageRequestData
import com.fim.prototype.mish.utils.PageResult
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ChapterRepo(
    private val chapterRepo: IChapterRepo,
    private val mongoBaseRepoUtils: MongoBaseRepoUtils
) {

    fun save(chapter: ChapterEntity): ChapterEntity {
        return chapterRepo.save(chapter)
    }

    fun getById(chapterId: String): ChapterEntity? {
        return chapterRepo.findByIdOrNull(chapterId)
    }

    fun listChapters(pageRequest: PageRequestData, filter: FilterBase): PageResult<ChapterEntity>{
       return mongoBaseRepoUtils.listPagedData(mongoBaseRepoUtils.createBaseFilterCriteriaAndReturnQuery(filter, pageRequest), pageRequest.page, ChapterEntity::class)
    }


}
package com.fim.prototype.mish.services

import com.fim.prototype.mish.data.ChapterEntity
import com.fim.prototype.mish.repo.ChapterRepo
import com.fim.prototype.mish.repo.GridFsRepo
import org.springframework.stereotype.Service


@Service
class ChapterService(
    private val chapterRepo: ChapterRepo,
    private val gridFs: GridFsRepo
) {

    fun createChapter(chapter: ChapterEntity): ChapterEntity {
        return chapterRepo.save(chapter)
    }

    fun getChapter(chapterId: String): ChapterEntity? {
        return chapterRepo.findById(chapterId).orElse(null)
    }
}


package com.fim.prototype.mish.services

import com.fim.prototype.mish.ChapterEntity
import com.fim.prototype.mish.repo.ChapterRepo
import org.springframework.stereotype.Service

@Service
class ChapterService(
    private val chapterRepo: ChapterRepo
) {

    fun createChapter(chapter: ChapterEntity): ChapterEntity {
        return chapterRepo.save(chapter)
    }

    fun getChapter(chapterId: String): ChapterEntity? {
        return chapterRepo.findById(chapterId).orElse(null)
    }
}


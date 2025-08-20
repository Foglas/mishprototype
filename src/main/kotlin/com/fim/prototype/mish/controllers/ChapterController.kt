package com.fim.prototype.mish.controllers

import com.fim.prototype.mish.data.entities.ChapterEntity
import com.fim.prototype.mish.services.ChapterService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/chapter")
class ChapterController(
    val chapterService: ChapterService
){

    @PostMapping("/create")
    fun createChapter(@RequestBody chapter: ChapterEntity): ChapterEntity  {
      return chapterService.createChapter(chapter)
    }

    @GetMapping("/{id}")
    fun getChapter(@PathVariable("id") chapterId: String): ChapterEntity {
       return chapterService.getChapter(chapterId)
    }

    @GetMapping("/search-fulltext")
    fun searchByFulltext(@RequestParam("keyword") keyword: String): List<ChapterEntity> {
        return chapterService.searchFullText(keyword)
    }
}
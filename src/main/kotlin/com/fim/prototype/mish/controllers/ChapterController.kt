package com.fim.prototype.mish.controllers

import com.fim.prototype.mish.data.entities.ChapterEntity
import com.fim.prototype.mish.data.rest.FullTextResult
import com.fim.prototype.mish.services.ChapterService
import com.fim.prototype.mish.utils.PageRequestData
import com.fim.prototype.mish.utils.PageResult
import org.springframework.data.domain.Sort
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
       return chapterService.getChapterById(chapterId)
    }

    @GetMapping("/list")
    fun listChapters(
        @RequestParam page: Int,
        @RequestParam limit: Int = 20,
        @RequestParam orderBy: String?=null,
        @RequestParam sortDirection: Sort.Direction = Sort.Direction.DESC,
    ): PageResult<ChapterEntity> {
        return chapterService.getAllChapters(PageRequestData(page, limit, orderBy, sortDirection))
    }

    @GetMapping("/search-fulltext")
    fun searchByFulltext(@RequestParam("keyword") keyword: String): FullTextResult {
        return chapterService.searchFullText(keyword)
    }

    @PutMapping("/update")
    fun updateChapter(@RequestBody chapter: ChapterEntity): ChapterEntity {
        return chapterService.updateChapter(chapter)
    }

}
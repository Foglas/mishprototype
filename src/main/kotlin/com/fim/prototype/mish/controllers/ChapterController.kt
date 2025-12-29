package com.fim.prototype.mish.controllers

import com.fim.prototype.mish.model.common.filters.FilterBase
import com.fim.prototype.mish.model.entities.ChapterEntity
import com.fim.prototype.mish.model.rest.FullTextResult
import com.fim.prototype.mish.properties.PageProperties
import com.fim.prototype.mish.services.chapters.ChapterService
import com.fim.prototype.mish.utils.PageRequestData
import com.fim.prototype.mish.utils.PageResult
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*
import java.time.Instant


@RestController
@RequestMapping("/api/chapter")
class ChapterController(
    private val chapterService: ChapterService,
    private val pageProperties: PageProperties,
) {

    @PostMapping("/create")
    fun createChapter(@RequestBody chapter: ChapterEntity): ChapterEntity {
        return chapterService.createChapter(chapter)
    }

    @GetMapping("/{id}")
    fun getChapter(@PathVariable("id") chapterId: String): ChapterEntity {
        return chapterService.getChapterById(chapterId)
    }

    @DeleteMapping("/{id}/delete")
    fun deleteChapter(@PathVariable("id") chapterId: String) {
        chapterService.delete(chapterId)
    }

    @GetMapping("/list")
    fun listChapters(
        @RequestParam page: Int,
        @RequestParam limit: Int = pageProperties.limit,
        @RequestParam orderBy: String? = null,
        @RequestParam sortDirection: Sort.Direction = pageProperties.sortDirection,
        @RequestParam name: String? = null,
        @RequestParam creatorId: String? = null,
        @RequestParam createdFrom: Instant? = null,
        @RequestParam createdTo: Instant? = null,
    ): PageResult<ChapterEntity> {
        return chapterService.getAllChapters(PageRequestData(page, limit, orderBy, sortDirection), FilterBase(name, creatorId, createdFrom, createdTo))
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
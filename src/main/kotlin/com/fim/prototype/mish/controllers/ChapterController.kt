package com.fim.prototype.mish.controllers

import com.fim.prototype.mish.model.common.filters.FilterBase
import com.fim.prototype.mish.model.entities.ChapterEntity
import com.fim.prototype.mish.model.rest.FullTextResult
import com.fim.prototype.mish.properties.PageProperties
import com.fim.prototype.mish.services.chapters.ChapterService
import com.fim.prototype.mish.utils.PageRequestData
import com.fim.prototype.mish.utils.PageResult
import com.fim.prototype.mish.utils.logger
import org.springframework.data.domain.Sort
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.time.Instant


@RestController
@RequestMapping("/api/chapter")
class ChapterController(
    private val chapterService: ChapterService,
    private val pageProperties: PageProperties,
) {
    val log = logger()

    @PreAuthorize("hasRole(T(com.fim.prototype.mish.security.data.Roles).CREATE_CHAPTER)")
    @PostMapping("/create")
    fun createChapter(@RequestBody chapter: ChapterEntity): ChapterEntity {
        log.debug("ChapterController:createChapter() - name: ${chapter.name}")
        return chapterService.createChapter(chapter)
    }

    @PreAuthorize("hasRole(T(com.fim.prototype.mish.security.data.Roles).STUDENT_ACTION)")
    @GetMapping("/{id}")
    fun getChapter(@PathVariable("id") chapterId: String): ChapterEntity {
        log.debug("ChapterController:getChapter() - id: $chapterId")
        return chapterService.getChapterById(chapterId)
    }

    @PreAuthorize("hasRole(T(com.fim.prototype.mish.security.data.Roles).CREATE_CHAPTER)")
    @DeleteMapping("/{id}/delete")
    fun deleteChapter(@PathVariable("id") chapterId: String) {
        log.debug("ChapterController:deleteChapter() - id: $chapterId")
        chapterService.delete(chapterId)
    }

    @PreAuthorize("hasRole(T(com.fim.prototype.mish.security.data.Roles).STUDENT_ACTION)")
    @GetMapping("/list")
    fun listChapters(
        @RequestParam page: Int,
        @RequestParam limit: Int? = null,
        @RequestParam orderBy: String? = null,
        @RequestParam sortDirection: Sort.Direction?,
        @RequestParam name: String? = null,
        @RequestParam creatorId: String? = null,
        @RequestParam createdFrom: Instant? = null,
        @RequestParam createdTo: Instant? = null,
    ): PageResult<ChapterEntity> {
        log.debug("ChapterController:listChapters() - from: $createdFrom - to: $createdTo")
        return chapterService.getAllChapters(PageRequestData(page, limit?: pageProperties.limit, orderBy, sortDirection?: pageProperties.sortDirection), FilterBase(name, creatorId, createdFrom, createdTo))
    }

    @PreAuthorize("hasRole(T(com.fim.prototype.mish.security.data.Roles).STUDENT_ACTION)")
    @GetMapping("/search-fulltext")
    fun searchByFulltext(@RequestParam("keyword") keyword: String): FullTextResult {
        log.debug("ChapterController:searchByFulltext() - id: $keyword")
        return chapterService.searchFullText(keyword)
    }

    @PreAuthorize("hasRole(T(com.fim.prototype.mish.security.data.Roles).CREATE_CHAPTER)")
    @PutMapping("/update")
    fun updateChapter(@RequestBody chapter: ChapterEntity): ChapterEntity {
        log.debug("ChapterController:updateChapter() - id: ${chapter.id}")
        return chapterService.updateChapter(chapter)
    }
}
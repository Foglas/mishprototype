package com.fim.prototype.mish.controllers

import com.fim.prototype.mish.ChapterEntity
import com.fim.prototype.mish.services.ChapterService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/chapter")
class ChapterController(
    val chapterService: ChapterService
){

    @GetMapping("/create")
    fun createChapter(@RequestBody chapter: ChapterEntity): ResponseEntity<ChapterEntity>  {
      return ResponseEntity.ok(chapterService.createChapter(chapter))
    }

    @GetMapping("/{id}")
    fun getChapter(@PathVariable("id") chapterId: String): ResponseEntity<ChapterEntity> {
        val chapter = chapterService.getChapter(chapterId)
        return if (chapter != null){
            ResponseEntity.ok(chapter)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
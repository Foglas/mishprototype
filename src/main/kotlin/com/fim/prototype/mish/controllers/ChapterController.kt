package com.fim.prototype.mish.controllers

import com.fim.prototype.mish.data.ChapterEntity
import com.fim.prototype.mish.services.ChapterService
import org.springframework.core.io.InputStreamResource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/api/chapter")
class ChapterController(
    val chapterService: ChapterService
){

    @PostMapping("/create")
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

    @PostMapping("/upload")
    fun uploadFile(@RequestBody model: MultipartFile, @RequestParam("chapterId") chapterId: String): ResponseEntity<String> {
         val id = chapterService.uploadModel(model, chapterId)
        return ResponseEntity.ok("File was uploaded: $id")
    }

    @GetMapping("/download/{chapterId}")
    fun downloadFile(@PathVariable("chapterId") chapterId: String): ResponseEntity<InputStreamResource> {
        return ResponseEntity.ok(InputStreamResource(chapterService.getModel(chapterId)?.inputStream!!))
    }
}
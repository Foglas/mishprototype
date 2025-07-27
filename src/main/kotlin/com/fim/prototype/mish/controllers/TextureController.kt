package com.fim.prototype.mish.controllers

import com.fim.prototype.mish.data.TextureUpload
import com.fim.prototype.mish.services.ModelService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/texture")
class TextureController(
    override val modelService: ModelService,
) : DownloadController(modelService) {

    @PostMapping("/upload")
    fun uploadTexture(@RequestPart model: MultipartFile, @RequestPart metadata: TextureUpload): ResponseEntity<String> {
        val id = modelService.uploadTexture(model, metadata)
        return ResponseEntity.ok("File was uploaded: $id")
    }
}
package com.fim.prototype.mish.controllers

import com.fim.prototype.mish.model.rest.SimpleTextureData
import com.fim.prototype.mish.model.rest.TextureUpload
import com.fim.prototype.mish.services.chapters.ModelService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
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
    fun uploadTexture(@RequestPart texture: MultipartFile, @RequestPart metadata: TextureUpload): SimpleTextureData {
       return modelService.uploadTexture(texture, metadata)
    }

    @DeleteMapping("/{id}/delete")
    fun deleteTexture(@PathVariable("id") textureId: String){
        modelService.deleteTexture(textureId)
    }

}
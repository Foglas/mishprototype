package com.fim.prototype.mish.controllers

import com.fim.prototype.mish.data.TextureUpload
import com.fim.prototype.mish.data.models.entities.ModelMetadataEntity
import com.fim.prototype.mish.data.models.entities.TextureMetadata
import com.fim.prototype.mish.services.ModelService
import org.springframework.core.io.InputStreamResource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping(value = ["/api/model"])
class ModelController(
    val modelService: ModelService,
) {

    @PostMapping("/upload-model")
    fun uploadModel(@RequestPart model: MultipartFile, @RequestPart metadata: ModelMetadataEntity): String {
       return modelService.uploadModel(model, metadata).toHexString()
    }

    @PostMapping("/upload-texture")
    fun uploadTexture(@RequestPart model: MultipartFile, @RequestPart metadata: TextureUpload): ResponseEntity<String> {
        val id = modelService.uploadTexture(model, metadata)
        return ResponseEntity.ok("File was uploaded: $id")
    }

    //TODO assign model to target

    //TODO get objectId, name, .png .jpg  - select models

    //TODO implement endpoint which can assign metadata to model
    //fun assignMetadata()

    //TODO implement endpoint for getting all related files to some item

    @PostMapping("/download/{itemId}")
    fun downloadFile(@PathVariable itemId: String): InputStreamResource {
        return InputStreamResource(modelService.getFileById(itemId)?.inputStream!!)
    }
}
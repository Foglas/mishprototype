package com.fim.prototype.mish.controllers

import com.fim.prototype.mish.services.FileStorageService
import org.springframework.core.io.InputStreamResource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping(value = ["/api/model"])
class ModelController(
    val fileStorageService: FileStorageService,
) {

    @PostMapping("/upload")
    fun uploadFile(@RequestBody model: MultipartFile): ResponseEntity<String> {
        val id = fileStorageService.uploadFile(model)
        return ResponseEntity.ok("File was uploaded: $id")
    }

    //TODO assign model to target

    //TODO get objectId, name, .png .jpg  - select models

    //TODO implement endpoint which can assign metadata to model
    //fun assignMetadata()

    //TODO implement endpoint for getting all related files to some item

    @PostMapping("/download/{itemId}")
    fun downloadFile(@PathVariable itemId: String): ResponseEntity<InputStreamResource> {
        return ResponseEntity.ok(InputStreamResource(fileStorageService.getFileById(itemId)?.inputStream!!))
    }

}
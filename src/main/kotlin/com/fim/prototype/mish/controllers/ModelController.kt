package com.fim.prototype.mish.controllers

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

    @PostMapping("/upload")
    fun uploadFile(@RequestBody model: MultipartFile, @RequestParam("key") key: String): ResponseEntity<String> {
        val id = modelService.uploadModel(model, key)
        return ResponseEntity.ok("File was uploaded: $id")
    }

    //TODO implement endpoint which can assign metadata to model
    //fun assignMetadata()

    //TODO implement endpoint for getting all related files to some item

    @PostMapping("/download/{itemId}")
    fun downloadFile(@PathVariable itemId: String, @RequestBody(required = false) metadata: Metadata?): ResponseEntity<InputStreamResource> {
        return ResponseEntity.ok(InputStreamResource(modelService.getModel(itemId)?.inputStream!!))
    }
}
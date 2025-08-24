package com.fim.prototype.mish.controllers

import com.fim.prototype.mish.data.entities.ModelIds
import com.fim.prototype.mish.data.entities.ModelMetadataEntity
import com.fim.prototype.mish.services.ModelService
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping(value = ["/api/model"])
class ModelController(
    override val modelService: ModelService,
) : DownloadController(modelService) {

    @PostMapping("/upload")
    fun uploadModel(@RequestPart model: MultipartFile, @RequestPart metadata: ModelMetadataEntity): ModelIds {
        return modelService.uploadModel(model, metadata)
    }

    @GetMapping("/list-by")
    fun listModelsMetadata(
        @RequestParam includeOtherTextures: Boolean = false,
    ): List<ModelIds>{
        return modelService.listModelMetadata(includeOtherTextures)
    }

    //TODO assign model to target

    //TODO get objectId, name, .png .jpg  - select models

    //TODO implement endpoint which can assign metadata to model
    //fun assignMetadata()

    //TODO implement endpoint for getting all related files to some item

}
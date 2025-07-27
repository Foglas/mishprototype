package com.fim.prototype.mish.controllers

import com.fim.prototype.mish.data.models.entities.ModelMetadataEntity
import com.fim.prototype.mish.services.ModelService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping(value = ["/api/model"])
class ModelController(
    override val modelService: ModelService,
) : DownloadController(modelService) {

    @PostMapping("/upload")
    fun uploadModel(@RequestPart model: MultipartFile, @RequestPart metadata: ModelMetadataEntity): String {
        return modelService.uploadModel(model, metadata).toHexString()
    }


    //TODO assign model to target

    //TODO get objectId, name, .png .jpg  - select models

    //TODO implement endpoint which can assign metadata to model
    //fun assignMetadata()

    //TODO implement endpoint for getting all related files to some item

}
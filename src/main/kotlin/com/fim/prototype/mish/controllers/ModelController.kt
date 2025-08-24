package com.fim.prototype.mish.controllers

import com.fim.prototype.mish.data.entities.ModelIds
import com.fim.prototype.mish.data.entities.ModelMetadataEntity
import com.fim.prototype.mish.services.ModelService
import com.fim.prototype.mish.utils.PageRequestData
import com.fim.prototype.mish.utils.PageResult
import org.springframework.data.domain.Sort
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
        @RequestParam page: Int,
        @RequestParam limit: Int = 20,
        @RequestParam orderBy: String?=null,
        @RequestParam sortDirection: Sort.Direction = Sort.Direction.DESC,
    ): PageResult<ModelIds>{
        return modelService.listModelMetadata(PageRequestData(page, limit, orderBy, sortDirection))
    }

    //TODO assign model to target

    //TODO get objectId, name, .png .jpg  - select models

    //TODO implement endpoint which can assign metadata to model
    //fun assignMetadata()

    //TODO implement endpoint for getting all related files to some item

}
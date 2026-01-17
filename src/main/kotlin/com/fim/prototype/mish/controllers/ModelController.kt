package com.fim.prototype.mish.controllers

import com.fim.prototype.mish.model.entities.ModelIds
import com.fim.prototype.mish.model.entities.ModelMetadataEntity
import com.fim.prototype.mish.properties.PageProperties
import com.fim.prototype.mish.services.chapters.ModelService
import com.fim.prototype.mish.utils.PageRequestData
import com.fim.prototype.mish.utils.PageResult
import org.springframework.data.domain.Sort
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping(value = ["/api/model"])
class ModelController(
    override val modelService: ModelService,
    private val pageProperties: PageProperties,
) : DownloadController(modelService) {

    @PreAuthorize("hasRole(T(com.fim.prototype.mish.security.data.Roles).TEACHER)")
    @PostMapping("/upload")
    fun uploadModel(@RequestPart model: MultipartFile, @RequestPart metadata: ModelMetadataEntity): ModelIds {
        return modelService.uploadModel(model, metadata)
    }

    @PreAuthorize("hasRole(T(com.fim.prototype.mish.security.data.Roles).TEACHER)")
    @DeleteMapping("/{id}/delete")
    fun deleteModel(@PathVariable("id") modelId: String){
        modelService.deleteModel(modelId)
    }

    @PreAuthorize("hasRole(T(com.fim.prototype.mish.security.data.Roles).TEACHER)")
    @GetMapping("/list-by")
    fun listModelsMetadata(
        @RequestParam page: Int,
        @RequestParam limit: Int? = null,
        @RequestParam orderBy: String?=null,
        @RequestParam sortDirection: Sort.Direction? = null,
    ): PageResult<ModelIds>{
        return modelService.listModelMetadata(PageRequestData(page, limit ?: pageProperties.limit, orderBy, sortDirection?: pageProperties.sortDirection))
    }

    //TODO assign model to target

    //TODO get objectId, name, .png .jpg  - select models

    //TODO implement endpoint which can assign metadata to model
    //fun assignMetadata()

    //TODO implement endpoint for getting all related files to some item

}
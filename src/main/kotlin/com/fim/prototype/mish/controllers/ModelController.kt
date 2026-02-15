package com.fim.prototype.mish.controllers

import com.fim.prototype.mish.model.common.FileEntityTree
import com.fim.prototype.mish.model.common.ModelMetadata
import com.fim.prototype.mish.model.entities.InputFileDesc
import com.fim.prototype.mish.model.entities.ModelIds
import com.fim.prototype.mish.properties.PageProperties
import com.fim.prototype.mish.services.FileService
import com.fim.prototype.mish.services.chapters.ModelService
import com.fim.prototype.mish.utils.PageRequestData
import com.fim.prototype.mish.utils.PageResult
import kotlinx.coroutines.runBlocking
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping(value = ["/api/model"])
class ModelController(
    override val fileService: FileService,
    private val modelService: ModelService,
    private val pageProperties: PageProperties,
) : DownloadController(fileService) {

    @PostMapping("/upload")
    fun uploadModel(
        @RequestPart files: List<MultipartFile>,
        @RequestPart metadata: InputFileDesc,
        @RequestPart modelMetadata: ModelMetadata): ModelIds = runBlocking {
        return@runBlocking modelService.uploadModel(files, metadata, modelMetadata)
    }

    @GetMapping("/{id}")
    fun getModelMetadataById(@PathVariable id: String): FileEntityTree {
        return modelService.getModelRelatedTree(id)
    }

    @DeleteMapping("/{id}/delete")
    fun deleteModel(@PathVariable("id") modelId: String, @RequestParam force: Boolean = false){
        modelService.deleteModel(modelId)
    }

    @PutMapping("/{id}/update")
    fun updateModel(@RequestPart files: List<MultipartFile>, @RequestPart metadata: InputFileDesc, @RequestPart(required = false) isAdvanced: String? = "false"){

    }

    @GetMapping("/list-by")
    fun listModelsMetadata(
        @RequestParam page: Int,
        @RequestParam limit: Int = pageProperties.limit,
        @RequestParam orderBy: String?=null,
        @RequestParam sortDirection: Sort.Direction = pageProperties.sortDirection,
    ): PageResult<ModelIds>{
        return modelService.listModelMetadata(PageRequestData(page, limit, orderBy, sortDirection))
    }
}
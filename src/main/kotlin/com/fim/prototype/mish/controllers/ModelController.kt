package com.fim.prototype.mish.controllers

import com.fim.prototype.mish.model.common.FileEntityTree
import com.fim.prototype.mish.model.common.ModelMetadata
import com.fim.prototype.mish.model.common.UpdateModelMetadata
import com.fim.prototype.mish.model.entities.InputFileDesc
import com.fim.prototype.mish.model.entities.ModelIds
import com.fim.prototype.mish.properties.PageProperties
import com.fim.prototype.mish.services.FileService
import com.fim.prototype.mish.services.chapters.ModelService
import com.fim.prototype.mish.utils.PageRequestData
import com.fim.prototype.mish.utils.PageResult
import kotlinx.coroutines.runBlocking
import org.springframework.data.domain.Sort
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping(value = ["/api/model"])
class ModelController(
    override val fileService: FileService,
    private val modelService: ModelService,
    private val pageProperties: PageProperties,
) : DownloadController(fileService) {

    @PreAuthorize("hasRole(T(com.fim.prototype.mish.security.data.Roles).CREATE_CHAPTER)")
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

    @PreAuthorize("hasRole(T(com.fim.prototype.mish.security.data.Roles).CREATE_CHAPTER)")
    @DeleteMapping("/{id}/delete")
    fun deleteModel(@PathVariable("id") modelId: String, @RequestParam force: Boolean = false){
        modelService.deleteModel(modelId)
    }

    @PreAuthorize("hasRole(T(com.fim.prototype.mish.security.data.Roles).STUDENT_ACTION)")
    @GetMapping("/list")
    @PutMapping("/update")
    fun updateModel(@RequestPart files: List<MultipartFile>, @RequestPart metadata: InputFileDesc, @RequestPart(required = false) modelMetadata: UpdateModelMetadata): ModelIds {
        return modelService.updateModel(files, metadata, modelMetadata)
    }

    @GetMapping("/list-by")
    fun listModelsMetadata(
        @RequestParam page: Int,
        @RequestParam limit: Int? = null,
        @RequestParam orderBy: String?=null,
        @RequestParam sortDirection: Sort.Direction? = null,
    ): PageResult<ModelIds>{
        return modelService.listModelMetadata(PageRequestData(page, limit ?: pageProperties.limit, orderBy, sortDirection?: pageProperties.sortDirection))
    }
}
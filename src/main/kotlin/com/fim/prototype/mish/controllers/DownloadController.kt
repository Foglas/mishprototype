package com.fim.prototype.mish.controllers

import com.fim.prototype.mish.services.ModelService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable


open class DownloadController(
    open val modelService: ModelService
) {

    //TODO should be removed after global security will be implemented..
    @CrossOrigin(origins = ["http://localhost:8081"])
    @GetMapping("/download/{itemId}")
    fun downloadFile(@PathVariable itemId: String, response: HttpServletResponse) {
        val resource = modelService.getFileById(itemId)

        response.contentType = resource.contentType
        response.setHeader("Content-Disposition", "attachment; filename=\"${resource.filename}\"")

        return resource.inputStream.use { input ->
            response.outputStream.use {
                input.copyTo(it)
            }
        }
    }
}
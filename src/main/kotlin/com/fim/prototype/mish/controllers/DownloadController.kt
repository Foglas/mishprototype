package com.fim.prototype.mish.controllers

import com.fim.prototype.mish.services.FileService
import com.fim.prototype.mish.utils.logger
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable


open class DownloadController(
    open val fileService: FileService
) {

    open val log = logger()

    @GetMapping("/download/{itemId}")
    fun downloadFile(@PathVariable itemId: String, response: HttpServletResponse) {
        log.debug("DownloadController:downloadFile() - itemId: $itemId")

        val resource = fileService.getFileById(itemId)

        response.contentType = resource.contentType
        response.setHeader("Content-Disposition", "attachment; filename=\"${resource.filename}\"")
        response.setHeader("Content-Length", resource.contentLength().toString())

        return resource.inputStream.use { input ->
            response.outputStream.use {
                input.copyTo(it)
            }
        }
    }
}
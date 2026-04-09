package com.fim.prototype.mish.exceptions.handlers

import com.fim.prototype.mish.exceptions.HttpException
import com.fim.prototype.mish.exceptions.data.HttpExceptionResponse
import com.fim.prototype.mish.utils.logger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {

    val log = logger()

    @ExceptionHandler(HttpException::class)
    fun handleNotFoundException(ex: HttpException): ResponseEntity<HttpExceptionResponse> {
        log.warn("Handled HttpException: status=${ex.httpStatus}, message=${ex.message}")
        return createExceptionResponseEntity(ex)
    }

    private fun createExceptionResponseEntity(ex: HttpException): ResponseEntity<HttpExceptionResponse>{
        return ResponseEntity.status(ex.httpStatus).body(ex.getExceptionHttpResponse())
    }
}
package com.fim.prototype.mish.exceptions.handlers

import com.fim.prototype.mish.exceptions.HttpException
import com.fim.prototype.mish.exceptions.data.HttpExceptionResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(HttpException::class)
    fun handleNotFoundException(ex: HttpException): ResponseEntity<HttpExceptionResponse> {
        return createExceptionResponseEntity(ex)
    }

    private fun createExceptionResponseEntity(ex: HttpException): ResponseEntity<HttpExceptionResponse>{
        return ResponseEntity.status(ex.httpStatus).body(ex.getExceptionHttpResponse())
    }
}
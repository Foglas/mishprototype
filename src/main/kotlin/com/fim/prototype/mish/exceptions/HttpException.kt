package com.fim.prototype.mish.exceptions

import com.fim.prototype.mish.exceptions.data.HttpExceptionResponse
import org.springframework.http.HttpStatus
import java.time.Instant
import java.util.Date

open class HttpException(
    override val message: String,
    val httpStatus: HttpStatus,
    open val targetErrorObject: Any?= null
) : RuntimeException(message){
    private val time = Instant.now()

    fun getExceptionHttpResponse() = HttpExceptionResponse(message, Date.from(time), targetErrorObject)
}
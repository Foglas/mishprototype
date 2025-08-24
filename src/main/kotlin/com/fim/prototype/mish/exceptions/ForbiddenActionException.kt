package com.fim.prototype.mish.exceptions

import com.fim.prototype.mish.exceptions.data.HttpExceptionResponse
import org.springframework.http.HttpStatus

class ForbiddenActionException(
    override val message: String,
    override val targetErrorObject: Any?= null
): HttpException(message, HttpStatus.FORBIDDEN, targetErrorObject) {
}
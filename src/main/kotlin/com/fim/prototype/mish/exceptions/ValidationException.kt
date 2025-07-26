package com.fim.prototype.mish.exceptions

import org.springframework.http.HttpStatus

class ValidationException(
    override val message: String,
    override val targetErrorObject: Any? = null
): HttpException(message, HttpStatus.BAD_REQUEST, targetErrorObject) {
}
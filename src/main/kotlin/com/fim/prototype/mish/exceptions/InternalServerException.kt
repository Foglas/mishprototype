package com.fim.prototype.mish.exceptions

import org.springframework.http.HttpStatus

class InternalServerException(
    override val message: String,
    override val targetErrorObject: Any? = null
): HttpException(message, HttpStatus.INTERNAL_SERVER_ERROR, targetErrorObject)
package com.fim.prototype.mish.exceptions

import org.springframework.http.HttpStatus

class NotFoundException(
    override val message: String,
    override val targetErrorObject: Any?= null
) : HttpException(message, HttpStatus.NOT_FOUND, targetErrorObject)
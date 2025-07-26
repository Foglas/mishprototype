package com.fim.prototype.mish.exceptions.data

import java.util.Date

data class HttpExceptionResponse(
    val message: String,
    val time: Date,
    val targetErrorObject: Any?= null
)

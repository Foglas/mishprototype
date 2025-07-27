package com.fim.prototype.mish.data.models

data class FileResponse<T>(
    val fileBase64: String,
    val metadata: T
)

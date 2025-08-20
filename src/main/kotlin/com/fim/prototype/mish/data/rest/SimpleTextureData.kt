package com.fim.prototype.mish.data.rest

data class SimpleTextureData(
    val textureFileId: String,
    val name: String,
    val csvContent: String = "",
)

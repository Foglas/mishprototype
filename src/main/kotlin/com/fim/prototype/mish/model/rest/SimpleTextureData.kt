package com.fim.prototype.mish.model.rest

data class SimpleTextureData(
    val textureFileId: String,
    val name: String,
    val csvContent: String = "",
)

package com.fim.prototype.mish.data.rest

import com.fim.prototype.mish.data.entities.TextureMetadata

data class TextureUpload(
    val modelId: String,
    val isPrimary: Boolean,
    val texture: TextureMetadata
)

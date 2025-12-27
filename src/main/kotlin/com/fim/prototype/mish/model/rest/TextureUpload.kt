package com.fim.prototype.mish.model.rest

import com.fim.prototype.mish.model.entities.TextureMetadata

data class TextureUpload(
    val modelId: String,
    val isPrimary: Boolean,
    val texture: TextureMetadata
)

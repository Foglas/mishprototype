package com.fim.prototype.mish.data

import com.fim.prototype.mish.data.models.entities.TextureMetadata

data class TextureUpload(
    val modelId: String,
    val isPrimary: Boolean,
    val texture: TextureMetadata
)

package com.fim.prototype.mish.model.rest

import com.fim.prototype.mish.model.entities.FileEntity

data class TextureUpload(
    val modelId: String,
    val isPrimary: Boolean,
    val texture: FileEntity
)

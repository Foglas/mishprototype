package com.fim.prototype.mish.data.model

import java.time.Instant

abstract class BasicModelMetadata {
    abstract val name: String
    abstract val created: Instant
    abstract val updated: Instant?
    abstract val targetFileId: String
}

data class TextureMetadata(
    override val name: String,
    override val created: Instant = Instant.now(),
    override val updated: Instant? = null,
    override val targetFileId: String,
    val csv: String? = null,
) : BasicModelMetadata()

data class ModelMetadata(
    override val name: String,
    override val created: Instant = Instant.now(),
    override val updated: Instant? = null,
    override val targetFileId: String,
    val creatorId: String,
    val mainTexture: TextureMetadata,
    val otherTextures: List<TextureMetadata>,
): BasicModelMetadata()


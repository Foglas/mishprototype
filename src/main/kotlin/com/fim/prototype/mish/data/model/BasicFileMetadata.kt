package com.fim.prototype.mish.data.model

import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

abstract class BasicFileMetadata {
    abstract val name: String
    abstract val created: Instant
    abstract val updated: Instant?
    abstract val targetFileId: String
    abstract val otherMetadata: String
}

data class TextureMetadata(
    override val name: String,
    override val created: Instant = Instant.now(),
    override val updated: Instant? = null,
    override val targetFileId: String,
    override val otherMetadata: String,
    val csvId: String? = null, //id of the csv file in gridFs
) : BasicFileMetadata()

@Document(collection = "models")
data class ModelMetadata(
    override val targetFileId: String, //modelId gridFs
    override val name: String,
    override val created: Instant = Instant.now(),
    override val updated: Instant? = null,
    override val otherMetadata: String,
    val creatorId: String,
    val mainTexture: TextureMetadata,
    val otherTextures: List<TextureMetadata>,
): BasicFileMetadata()


package com.fim.prototype.mish.data.models.entities

import com.fim.prototype.mish.data.models.BasicFileMetadata
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "models")
data class ModelMetadataEntity(
    override val targetFileId: String, //modelId gridFs
    override val name: String,
    override val created: Instant = Instant.now(),
    override val updated: Instant? = null,
    override val otherMetadata: String,
    val creatorId: String,
    val mainTexture: TextureMetadata,
    val otherTextures: List<TextureMetadata>,
): BasicFileMetadata()

data class TextureMetadata(
    override val name: String,
    override val created: Instant = Instant.now(),
    override val updated: Instant? = null,
    override val targetFileId: String,
    override val otherMetadata: String,
    val csvId: String? = null, //id of the csv file in gridFs
) : BasicFileMetadata()

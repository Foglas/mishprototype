package com.fim.prototype.mish.data.models.entities

import com.fim.prototype.mish.data.models.BasicFileMetadata
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.index.TextIndexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "models")
data class ModelMetadataEntity(
    @Id val id: String? = null,
    @Indexed(unique = true) override var targetFileId: String? = null, //modelId gridFs
    @TextIndexed override val name: String,
    override val created: Instant = Instant.now(),
    override val updated: Instant? = null,
    override val otherMetadata: String = "",
    @Indexed val creatorId: String? = null,
    var mainTexture: TextureMetadata?= null,
    var otherTextures: MutableList<TextureMetadata> = mutableListOf(),
): BasicFileMetadata(){
}

data class TextureMetadata(
    override var targetFileId: String?= null, //textureId gridFs
    override val name: String,
    override val created: Instant = Instant.now(),
    override val updated: Instant? = null,
    override val otherMetadata: String = "",
    val csvContent: String? = null,
) : BasicFileMetadata()

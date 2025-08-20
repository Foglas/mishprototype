package com.fim.prototype.mish.data.entities

import com.fim.prototype.mish.data.rest.SimpleTextureData
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.TextIndexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "chapters")
data class ChapterEntity(
    @Id val id: String? = null,
    @TextIndexed val name: String,
    val creatorId: String? = null,
    @TextIndexed val content: String,  //editor.js
    val models: List<ModelIds>,
    val created: Instant = Instant.now(),
    val updated: Instant? = null,
)

data class ModelIds(
    val model: FileIdWithName,
    val mainTexture: SimpleTextureData? = null,
    val otherTextures: List<SimpleTextureData> = listOf(),
)

data class FileIdWithName(
    val id: String,
    val name: String,
)


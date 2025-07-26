package com.fim.prototype.mish.data

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "chapters")
data class ChapterEntity(
    @Id val id: String? = null,
    val name: String,
    val creatorId: String? = null,
    val content: String,  //editor.js
    val models: List<ModelIds>,
    val created: Instant = Instant.now(),
    val updated: Instant? = null,
)

data class ModelIds(
    val model: FileIdWithName,
    val mainTexture: FileIdWithName,
    val otherTextures: List<FileIdWithName> = listOf(),
)

data class FileIdWithName(
    val id: String,
    val name: String,
)


package com.fim.prototype.mish.model.entities

import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import com.fim.prototype.mish.repo.MongoCollection
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = MongoCollection.CHAPTER_ENTITY)
data class ChapterEntity(
    val content: String,  //editor.js
    val models: List<ModelIds>,
    @JsonSetter(nulls = Nulls.SKIP) var creatorId: String? = null,
    @JsonSetter(nulls = Nulls.SKIP) var id: String? = null,
    @JsonSetter(nulls = Nulls.SKIP) var description: String? = null,
    var name: String,
    @JsonSetter(nulls = Nulls.SKIP) var created: Instant? = Instant.now(),
    @JsonSetter(nulls = Nulls.SKIP) var updated: Instant? = Instant.now(),
)

data class ModelIds(
    val metadataId: String,
    val model: FileIdWithName,
    @JsonSetter(nulls = Nulls.SKIP) val description: String? = null
)

data class FileIdWithName(
    val id: String,
    val name: String,
    val senseType: FileSenseType,
    val related: List<FileIdWithName> = emptyList()
)


package com.fim.prototype.mish.model.entities

import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import com.fim.prototype.mish.model.entities.abstracts.AbstractEntity
import com.fim.prototype.mish.repo.MongoCollection
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = MongoCollection.CHAPTER_ENTITY)
data class ChapterEntity(
    val content: String,  //editor.js
    val models: List<ModelIds>,
    @JsonSetter(nulls = Nulls.SKIP) override var creatorId: String? = null,
    @JsonSetter(nulls = Nulls.SKIP) override var id: String? = null,
    @JsonSetter(nulls = Nulls.SKIP) override var description: String? = null,
    override var name: String,
    @JsonSetter(nulls = Nulls.SKIP) override var created: Instant? = Instant.now(),
    @JsonSetter(nulls = Nulls.SKIP) override var updated: Instant? = Instant.now(),
): AbstractEntity()

data class ModelIds(
    val metadataId: String,
    val model: FileIdWithName,
    val mainTexture: FileIdWithName? = null,
    val otherTextures: List<FileIdWithName> = listOf(),
)

data class FileIdWithName(
    val id: String,
    val name: String,
    val related: List<FileIdWithName> = emptyList()
)


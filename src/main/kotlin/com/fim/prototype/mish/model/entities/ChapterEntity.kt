package com.fim.prototype.mish.model.entities

import com.fim.prototype.mish.model.entities.abstracts.AbstractEntity
import com.fim.prototype.mish.repo.MongoCollection
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = MongoCollection.CHAPTER_ENTITY)
data class ChapterEntity(
    val content: String,  //editor.js
    val models: List<ModelIds>,
    override var creatorId: String? = null,
    override var id: String? = null,
    override var description: String,
    override var name: String,
    override var created: Instant,
    override var updated: Instant,
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
)


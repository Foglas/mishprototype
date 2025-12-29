package com.fim.prototype.mish.model.entities

import com.fim.prototype.mish.model.entities.abstracts.AbstractEntity
import com.fim.prototype.mish.model.rest.SimpleTextureData
import com.fim.prototype.mish.services.fulltext.FullTextSearchingService
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.TextIndexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = FullTextSearchingService.chapter)
data class ChapterEntity(
    @TextIndexed val content: String,  //editor.js
    val models: List<ModelIds>,
): AbstractEntity()

data class ModelIds(
    val metadataId: String,
    val model: FileIdWithName,
    val mainTexture: SimpleTextureData? = null,
    val otherTextures: List<SimpleTextureData> = listOf(),
)

data class FileIdWithName(
    val id: String,
    val name: String,
)


package com.fim.prototype.mish.data

import com.fim.prototype.mish.data.model.ModelMetadata
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "chapters")
data class ChapterEntity(
    @Id val id: String? = null,
    val name: String,
    val creatorId: String,
    val content: String,  //editor.js
    val models: List<ModelMetadata>,
    val created: Instant = Instant.now(),
    val updated: Instant? = null,
)

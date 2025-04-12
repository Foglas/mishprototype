package com.fim.prototype.mish.data

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "chapters")
data class ChapterEntity(
    @Id val id: String? = null,
    val header: String,
    val content: String,
    val modelPath: String = "",
    val childChapter: ChapterEntity?= null
)

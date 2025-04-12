package com.fim.prototype.mish

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "chapters")
data class ChapterEntity(
    @Id val id: String? = null,
    val header: String,
    val content: String,
    val childChapter: ChapterEntity?= null
)

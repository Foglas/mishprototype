package com.fim.prototype.mish.model.rest

import com.fim.prototype.mish.model.entities.ChapterEntity

data class FullTextResult(
    val chapters: List<ChapterEntity>
)
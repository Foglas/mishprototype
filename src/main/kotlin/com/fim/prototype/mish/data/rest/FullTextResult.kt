package com.fim.prototype.mish.data.rest

import com.fim.prototype.mish.data.entities.ChapterEntity

data class FullTextResult(
    val chapters: List<ChapterEntity>
)
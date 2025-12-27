package com.fim.prototype.mish.model.entities.quiz.answers

import org.springframework.data.annotation.TypeAlias

@TypeAlias("TextureClickAnswerData")
data class TextureClickAnswerData(
    var modelId: String? = null,
    var textureId: String? = null,
    var hexColor: String? = null,
) : AbstractAnswerData()
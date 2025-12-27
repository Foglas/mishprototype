package com.fim.prototype.mish.model.entities.quiz.questions

import org.springframework.data.annotation.TypeAlias

@TypeAlias("TextureClickQuestionData")
data class TextureClickQuestionData(
    var modelId: String? = null,
    var textureId: String? = null
) : AbstractQuestionData()
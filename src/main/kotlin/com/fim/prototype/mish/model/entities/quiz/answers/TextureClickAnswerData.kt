package com.fim.prototype.mish.model.entities.quiz.answers

data class TextureClickAnswerData(
    var modelId: String? = null,
    var textureId: String? = null,
    var hexColor: String? = null,
) : AbstractAnswerData()
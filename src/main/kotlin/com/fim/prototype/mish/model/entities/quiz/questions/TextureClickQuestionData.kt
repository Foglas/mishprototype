package com.fim.prototype.mish.model.entities.quiz.questions

data class TextureClickQuestionData(
    var modelId: String? = null,
    var textureId: String? = null
) : AbstractQuestionData()
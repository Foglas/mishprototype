package com.fim.prototype.mish.model.entities.quiz.submission

data class TextureClickSubmissionData(
    var hexColor: String? = null,
    var modelId: String? = null,
    var textureId: String? = null,
) : AbstractSubmissionData()
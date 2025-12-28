package com.fim.prototype.mish.model.common

data class QuestionPartValidation(
    val questionId: String?,
    val isCorrect: Boolean,
    val points: Int = 0,
    val text: String = ""
)

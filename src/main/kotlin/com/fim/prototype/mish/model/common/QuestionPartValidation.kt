package com.fim.prototype.mish.model.common

import com.fim.prototype.mish.model.entities.quiz.submission.AbstractSubmissionData

data class QuestionPartValidation(
    val questionId: String?,
    val isCorrect: Boolean,
    val points: Int = 0,
    val text: String = "",
    val submission: AbstractSubmissionData? = null
)

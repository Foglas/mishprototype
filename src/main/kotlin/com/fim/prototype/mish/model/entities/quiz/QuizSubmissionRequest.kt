package com.fim.prototype.mish.model.entities.quiz

import com.fim.prototype.mish.model.entities.quiz.submission.AbstractSubmissionData

data class QuizSubmissionRequest(
    var quizId: String? = null,
    var answers: List<AbstractSubmissionData> = listOf()
)

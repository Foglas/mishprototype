package com.fim.prototype.mish.model.entities.quiz

data class QuizValidationResult(
    var totalScore: Int = 0,
    var maxScore: Int = 0,
    var percentage: Double = 0.0,
    var questionResults: Map<String, Boolean> = mapOf(),
    var questionScores: Map<String, Int> = mapOf(),
)

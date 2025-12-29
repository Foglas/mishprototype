package com.fim.prototype.mish.model.entities.quiz

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "quizValidationResult")
data class QuizValidationResultWithUser(
    val id: String? = null,
    val userId: String,
    val quizId: String,
    val name: String,
    var totalScore: Int = 0,
    var maxScore: Int = 0,
    var percentage: Double = 0.0,
    var questionResults: Map<String, Boolean> = mapOf(),
    var questionScores: Map<String, Int> = mapOf(),
)

fun QuizValidationResultWithUser.toQuizValidationResult(): QuizValidationResult{
    return QuizValidationResult(
        quizResultId = id,
        quizId = quizId,
        name = name,
        totalScore = totalScore,
        percentage = percentage,
        questionResults = questionResults,
        questionScores = questionScores,
    )
}
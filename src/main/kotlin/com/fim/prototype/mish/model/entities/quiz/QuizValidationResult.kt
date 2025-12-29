package com.fim.prototype.mish.model.entities.quiz

import org.springframework.data.annotation.Id

data class QuizValidationResult(
    @Id
    var quizResultId: String?=null,
    var quizId: String,
    val name: String,
    var totalScore: Int = 0,
    var maxScore: Int = 0,
    var percentage: Double = 0.0,
    var questionResults: Map<String, Boolean> = mapOf(),
    var questionScores: Map<String, Int> = mapOf(),
)

fun QuizValidationResult.toQuizValidationResultWithUser(
    userId: String
): QuizValidationResultWithUser {
    return QuizValidationResultWithUser(
        userId = userId,
        quizId = quizId,
        name = name,
        totalScore = this.totalScore,
        maxScore = this.maxScore,
        percentage = this.percentage,
        questionResults = this.questionResults,
        questionScores = this.questionScores
    )
}
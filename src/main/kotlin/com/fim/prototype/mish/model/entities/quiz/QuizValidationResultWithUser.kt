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
    val questionResults: List<QuizValidationQuestion> = listOf(),
    )

fun QuizValidationResultWithUser.toQuizValidationResult(): QuizValidationResult{

    return QuizValidationResult(
        id = id,
        quizId = quizId,
        name = name,
        totalScore = totalScore,
        percentage = percentage,
        questionResults = questionResults
    )
}
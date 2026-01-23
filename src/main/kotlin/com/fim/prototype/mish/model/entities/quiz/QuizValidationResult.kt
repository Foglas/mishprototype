package com.fim.prototype.mish.model.entities.quiz

import com.fim.prototype.mish.model.entities.quiz.submission.AbstractSubmissionData
import org.springframework.data.annotation.Id

data class QuizValidationResult(
    @Id
    var id: String?=null,
    var quizId: String,
    val name: String,
    var totalScore: Int = 0,
    var maxScore: Int = 0,
    var percentage: Double = 0.0,
    val questionResults: List<QuizValidationQuestion> = listOf(),
)

data class QuizValidationQuestion(
    val questionText: String,
    val isCorrect: Boolean,
    val points: Int,
    val submission: AbstractSubmissionData?=null
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
    )
}
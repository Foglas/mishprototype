package com.fim.prototype.mish.model.entities.quiz

data class QuickQuizResult(
    val id: String,
    val quizId: String,
    val userId: String,
    val name: String,
    val chapterName: String?=null,
    val maxScore: Int = 0,
    val totalScore: Int = 0,
    val percentage: Double = 0.0
)
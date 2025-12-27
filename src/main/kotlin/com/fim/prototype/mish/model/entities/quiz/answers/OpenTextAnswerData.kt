package com.fim.prototype.mish.model.entities.quiz.answers

data class OpenTextAnswerData(
    var acceptableAnswers: List<String> = listOf(),
    var exactMatch: Boolean? = null
) : AbstractAnswerData()
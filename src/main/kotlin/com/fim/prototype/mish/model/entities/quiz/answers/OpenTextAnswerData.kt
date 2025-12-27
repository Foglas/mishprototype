package com.fim.prototype.mish.model.entities.quiz.answers

import org.springframework.data.annotation.TypeAlias

@TypeAlias("OpenTextAnswerData")
data class OpenTextAnswerData(
    var acceptableAnswers: List<String> = listOf(),
    var exactMatch: Boolean? = null
) : AbstractAnswerData()
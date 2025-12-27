package com.fim.prototype.mish.model.entities.quiz.answers

import org.springframework.data.annotation.TypeAlias

@TypeAlias("MultipleChoiceAnswerData")
data class MultipleChoiceAnswerData(
    var correctItems: List<Int> = listOf()
) : AbstractAnswerData()
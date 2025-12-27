package com.fim.prototype.mish.model.entities.quiz.answers

data class MultipleChoiceAnswerData(
    var correctItems: List<Int> = listOf()
) : AbstractAnswerData()
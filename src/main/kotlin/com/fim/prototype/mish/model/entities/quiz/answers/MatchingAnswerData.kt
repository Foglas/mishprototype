package com.fim.prototype.mish.model.entities.quiz.answers

data class MatchingAnswerData(
    var correctMatches: Map<Int, Int> = mapOf()
) : AbstractAnswerData()
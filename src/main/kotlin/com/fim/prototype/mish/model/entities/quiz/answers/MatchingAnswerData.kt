package com.fim.prototype.mish.model.entities.quiz.answers

import org.springframework.data.annotation.TypeAlias

@TypeAlias("MatchingAnswerData")
data class MatchingAnswerData(
    var correctMatches: Map<Int, Int> = mapOf()
) : AbstractAnswerData()
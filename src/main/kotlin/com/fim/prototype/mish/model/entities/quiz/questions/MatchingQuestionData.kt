package com.fim.prototype.mish.model.entities.quiz.questions

import org.springframework.data.annotation.TypeAlias

@TypeAlias("MatchingQuestionData")
data class MatchingQuestionData(
    var leftItems: List<String>? = null,
    var rightItems: List<String>? = null
) : AbstractQuestionData()

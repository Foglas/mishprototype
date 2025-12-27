package com.fim.prototype.mish.model.entities.quiz.questions

data class MatchingQuestionData(
    var leftItems: List<String>? = null,
    var rightItems: List<String>? = null
) : AbstractQuestionData()

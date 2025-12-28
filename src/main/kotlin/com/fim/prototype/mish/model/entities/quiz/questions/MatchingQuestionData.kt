package com.fim.prototype.mish.model.entities.quiz.questions

data class MatchingQuestionData(
    var leftItems: List<String> = listOf(),
    var rightItems: List<String> = listOf()
) : AbstractQuestionData()

package com.fim.prototype.mish.model.entities.quiz.questions


data class SingleChoiceQuestionData(
    var options: List<String> = listOf()
) : AbstractQuestionData()
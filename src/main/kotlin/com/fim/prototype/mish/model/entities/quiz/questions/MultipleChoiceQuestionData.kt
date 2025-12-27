package com.fim.prototype.mish.model.entities.quiz.questions


data class MultipleChoiceQuestionData(
    var options: List<String>? = null
) : AbstractQuestionData()
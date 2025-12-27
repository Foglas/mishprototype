package com.fim.prototype.mish.model.entities.quiz.questions

data class OrderingQuestionData(
    var items: List<String>? = null
) : AbstractQuestionData()
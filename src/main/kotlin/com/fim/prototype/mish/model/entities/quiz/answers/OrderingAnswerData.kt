package com.fim.prototype.mish.model.entities.quiz.answers


data class OrderingAnswerData(
    var correctOrder: List<Int>? = null
): AbstractAnswerData()
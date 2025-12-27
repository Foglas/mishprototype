package com.fim.prototype.mish.model.entities.quiz.answers

import org.springframework.data.annotation.TypeAlias

@TypeAlias("OrderingAnswerData")
data class OrderingAnswerData(
    var correctOrder: List<Int>? = null
): AbstractAnswerData()
package com.fim.prototype.mish.model.entities.quiz.questions

import org.springframework.data.annotation.TypeAlias

@TypeAlias("OrderingQuestionData")
data class OrderingQuestionData(
    var items: List<String>? = null
) : AbstractQuestionData()
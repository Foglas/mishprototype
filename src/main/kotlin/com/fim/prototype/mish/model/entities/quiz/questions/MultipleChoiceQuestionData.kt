package com.fim.prototype.mish.model.entities.quiz.questions

import org.springframework.data.annotation.TypeAlias

@TypeAlias("MultipleChoiceQuestionData")
data class MultipleChoiceQuestionData(
    var options: List<String>? = null
) : AbstractQuestionData()
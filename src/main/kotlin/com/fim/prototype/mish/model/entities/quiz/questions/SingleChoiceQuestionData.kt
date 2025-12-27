package com.fim.prototype.mish.model.entities.quiz.questions

import org.springframework.data.annotation.TypeAlias

@TypeAlias("SingleChoiceQuestionData")
data class SingleChoiceQuestionData(
    var options: List<String>? = null
) : AbstractQuestionData()
package com.fim.prototype.mish.model.entities.quiz.answers

import org.springframework.data.annotation.TypeAlias

@TypeAlias("SingleChoiceAnswerData")
data class SingleChoiceAnswerData(
    var correctIndex: Int? = null
) : AbstractAnswerData()

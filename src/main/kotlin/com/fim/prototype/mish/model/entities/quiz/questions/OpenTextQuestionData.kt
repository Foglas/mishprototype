package com.fim.prototype.mish.model.entities.quiz.questions

import org.springframework.data.annotation.TypeAlias

@TypeAlias("OpenTextQuestionData")
class OpenTextQuestionData(
    var placeholder: String = ""
) : AbstractQuestionData()

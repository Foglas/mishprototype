package com.fim.prototype.mish.model.entities.quiz.answers

import com.fim.prototype.mish.model.entities.quiz.QuestionType

abstract class AbstractAnswerData {
    var questionId: String? = null
    var type: QuestionType? = null
}

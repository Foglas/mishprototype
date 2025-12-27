package com.fim.prototype.mish.model.entities.quiz.questions

import com.fim.prototype.mish.model.entities.quiz.QuestionType


abstract class AbstractQuestionData {
    var questionId: String? = null
    var questionText: String? = null
    var type: QuestionType? = null
    var points: Int? = null
}
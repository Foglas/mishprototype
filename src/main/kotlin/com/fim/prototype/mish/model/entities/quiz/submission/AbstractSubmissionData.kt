package com.fim.prototype.mish.model.entities.quiz.submission

import com.fim.prototype.mish.model.entities.quiz.QuestionType

abstract class AbstractSubmissionData {
    var questionId: String? = null
    var type: QuestionType? = null
}

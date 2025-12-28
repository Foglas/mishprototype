package com.fim.prototype.mish.services.quiz.validators

import com.fim.prototype.mish.model.entities.quiz.answers.AbstractAnswerData
import com.fim.prototype.mish.model.entities.quiz.submission.AbstractSubmissionData
import kotlin.reflect.KClass

interface QuizResultValidator {
    val type: KClass<*>
    fun validate(expected: AbstractAnswerData, actual: AbstractSubmissionData): Boolean
}
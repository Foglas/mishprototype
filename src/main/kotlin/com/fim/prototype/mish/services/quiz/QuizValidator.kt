package com.fim.prototype.mish.services.quiz

import com.fim.prototype.mish.model.entities.quiz.answers.AbstractAnswerData
import com.fim.prototype.mish.model.entities.quiz.submission.AbstractSubmissionData
import kotlin.reflect.KClass

interface QuizValidator {
    val type: KClass<*>
    fun validate(expected: AbstractAnswerData, actual: AbstractSubmissionData): Boolean
}
package com.fim.prototype.mish.services

import com.fim.prototype.mish.model.entities.quiz.answers.AbstractAnswerData
import kotlin.reflect.KClass

interface QuizValidator {
    val type: KClass<*>
    fun validate(expected: AbstractAnswerData, actual: AbstractAnswerData): Boolean
}
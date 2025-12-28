package com.fim.prototype.mish.services.quiz.questions

import com.fim.prototype.mish.model.entities.quiz.questions.AbstractQuestionData
import kotlin.reflect.KClass

interface QuestionValidator {
    val type: KClass<*>
    fun validate(question: AbstractQuestionData): AbstractQuestionData
}
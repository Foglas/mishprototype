package com.fim.prototype.mish.services.quiz.validators

import com.fim.prototype.mish.model.entities.quiz.answers.AbstractAnswerData
import com.fim.prototype.mish.model.entities.quiz.questions.AbstractQuestionData
import kotlin.reflect.KClass

interface CreateQuizValidator {
    val type: KClass<*>
    fun validate(question: AbstractQuestionData, answer: AbstractAnswerData): Pair<AbstractQuestionData, AbstractAnswerData>
}
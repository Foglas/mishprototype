package com.fim.prototype.mish.services

import com.fim.prototype.mish.model.entities.quiz.answers.*
import com.fim.prototype.mish.model.entities.quiz.submission.*
import org.springframework.stereotype.Service
import kotlin.reflect.KClass

@Service
class MultipleChoiceValidator : QuizValidator{
    override val type: KClass<*>
        get() = MultipleChoiceAnswerData::class

    override fun validate(expected: AbstractAnswerData, actual: AbstractSubmissionData): Boolean {
    return false
    }
}

@Service
class MatchingAnswerValidator : QuizValidator{
    override val type: KClass<*>
        get() = MatchingAnswerData::class

    override fun validate(expected: AbstractAnswerData, actual: AbstractSubmissionData): Boolean {
        val expectedTyped = expected as MatchingAnswerData
        val actualTyped = actual as MatchingSubmissionData
        expectedTyped.correctMatches
        return false
    }
}


@Service
class OpenTextAnswerValidator : QuizValidator {
    override val type: KClass<*>
        get() = OpenTextAnswerData::class

    override fun validate(expected: AbstractAnswerData, actual: AbstractSubmissionData): Boolean {
        val expectedTyped = expected as OpenTextAnswerData
        val actualTyped = actual as OpenTextSubmissionData
        return false
    }
}


@Service
class OrderingAnswerValidator : QuizValidator {
    override val type: KClass<*>
        get() = OrderingAnswerData::class

    override fun validate(expected: AbstractAnswerData, actual: AbstractSubmissionData): Boolean {
        val expectedTyped = expected as OrderingAnswerData
        val actualTyped = actual as OrderingSubmissionData
        return false
    }
}

@Service
class SingleChoiceAnswerValidator : QuizValidator {
    override val type: KClass<*>
        get() = SingleChoiceAnswerData::class

    override fun validate(expected: AbstractAnswerData, actual: AbstractSubmissionData): Boolean {
        val expectedTyped = expected as SingleChoiceAnswerData
        val actualTyped = actual as SingleChoiceSubmissionData
        return false
    }
}

@Service
class TextureClickAnswerValidator : QuizValidator {
    override val type: KClass<*>
        get() = TextureClickAnswerData::class

    override fun validate(expected: AbstractAnswerData, actual: AbstractSubmissionData): Boolean {
        val expectedTyped = expected as TextureClickAnswerData
        val actualTyped = actual as TextureClickSubmissionData
        return false
    }
}











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
        val expectedTyped = expected as MultipleChoiceAnswerData
        val actualTyped = actual as MultipleChoiceSubmissionData

        return expectedTyped.correctItems == actualTyped.selectedItems
    }
}

@Service
class MatchingAnswerValidator : QuizValidator{
    override val type: KClass<*>
        get() = MatchingAnswerData::class

    override fun validate(expected: AbstractAnswerData, actual: AbstractSubmissionData): Boolean {
        val expectedTyped = expected as MatchingAnswerData
        val actualTyped = actual as MatchingSubmissionData

        return  expectedTyped.correctMatches == actualTyped.matches
    }
}


@Service
class OpenTextAnswerValidator : QuizValidator {
    override val type: KClass<*>
        get() = OpenTextAnswerData::class

    override fun validate(expected: AbstractAnswerData, actual: AbstractSubmissionData): Boolean {
        val expectedTyped = expected as OpenTextAnswerData
        val actualTyped = actual as OpenTextSubmissionData

        //TODO exact matching?

        return expectedTyped.acceptableAnswers.contains(actualTyped.text.lowercase())
    }
}


@Service
class OrderingAnswerValidator : QuizValidator {
    override val type: KClass<*>
        get() = OrderingAnswerData::class

    override fun validate(expected: AbstractAnswerData, actual: AbstractSubmissionData): Boolean {
        val expectedTyped = expected as OrderingAnswerData
        val actualTyped = actual as OrderingSubmissionData

        return expectedTyped.correctOrder == actualTyped.order
    }
}

@Service
class SingleChoiceAnswerValidator : QuizValidator {
    override val type: KClass<*>
        get() = SingleChoiceAnswerData::class

    override fun validate(expected: AbstractAnswerData, actual: AbstractSubmissionData): Boolean {
        val expectedTyped = expected as SingleChoiceAnswerData
        val actualTyped = actual as SingleChoiceSubmissionData

        return expectedTyped.correctIndex == actualTyped.selectedIndex
    }
}

@Service
class TextureClickAnswerValidator : QuizValidator {
    override val type: KClass<*>
        get() = TextureClickAnswerData::class

    override fun validate(expected: AbstractAnswerData, actual: AbstractSubmissionData): Boolean {
        val expectedTyped = expected as TextureClickAnswerData
        val actualTyped = actual as TextureClickSubmissionData

        return expectedTyped.hexColor == actualTyped.hexColor
    }
}











package com.fim.prototype.mish.services.quiz

import com.fim.prototype.mish.model.entities.quiz.answers.*
import com.fim.prototype.mish.model.entities.quiz.submission.*
import org.springframework.stereotype.Service
import kotlin.reflect.KClass

@Service
class MultipleChoiceAnswerValidator : QuizValidator {
    override val type: KClass<*>
        get() = MultipleChoiceAnswerData::class

    override fun validate(expected: AbstractAnswerData, actual: AbstractSubmissionData): Boolean {
        val expectedTyped = expected.asMultipleChoiceAnswerData()
        val actualTyped = actual.asMultipleChoiceSubmissionData()

        return expectedTyped.correctItems == actualTyped.selectedItems
    }
}

@Service
class MatchingAnswerValidator : QuizValidator {
    override val type: KClass<*>
        get() = MatchingAnswerData::class

    override fun validate(expected: AbstractAnswerData, actual: AbstractSubmissionData): Boolean {
        val expectedTyped = expected.asMatchingAnswerData()
        val actualTyped = actual.asMatchingSubmissionData()

        return  expectedTyped.correctMatches == actualTyped.matches
    }
}


@Service
class OpenTextAnswerValidator : QuizValidator {
    override val type: KClass<*>
        get() = OpenTextAnswerData::class

    override fun validate(expected: AbstractAnswerData, actual: AbstractSubmissionData): Boolean {
        val expectedTyped = expected.asOpenTextAnswerData()
        val actualTyped = actual.asOpenTextSubmissionData()

        //TODO exact matching?

        return expectedTyped.acceptableAnswers.contains(actualTyped.text.lowercase())
    }
}


@Service
class OrderingAnswerValidator : QuizValidator {
    override val type: KClass<*>
        get() = OrderingAnswerData::class

    override fun validate(expected: AbstractAnswerData, actual: AbstractSubmissionData): Boolean {
        val expectedTyped = expected.asOrderingAnswerData()
        val actualTyped = actual.asOrderingSubmissionData()

        return expectedTyped.correctOrder == actualTyped.order
    }
}

@Service
class SingleChoiceAnswerValidator : QuizValidator {
    override val type: KClass<*>
        get() = SingleChoiceAnswerData::class

    override fun validate(expected: AbstractAnswerData, actual: AbstractSubmissionData): Boolean {
        val expectedTyped = expected.asSingleChoiceAnswerData()
        val actualTyped = actual.asSingleChoiceSubmissionData()

        return expectedTyped.correctIndex == actualTyped.selectedIndex
    }
}

@Service
class TextureClickAnswerValidator : QuizValidator {
    override val type: KClass<*>
        get() = TextureClickAnswerData::class

    override fun validate(expected: AbstractAnswerData, actual: AbstractSubmissionData): Boolean {
        val expectedTyped = expected.asTextureClickAnswerData()
        val actualTyped = actual.asTextureClickSubmissionData()

        return expectedTyped.hexColor == actualTyped.hexColor
    }
}











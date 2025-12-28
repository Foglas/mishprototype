package com.fim.prototype.mish.services.quiz.questions

import com.fim.prototype.mish.exceptions.NotFoundException
import com.fim.prototype.mish.exceptions.ValidationException
import com.fim.prototype.mish.model.entities.quiz.answers.*
import com.fim.prototype.mish.model.entities.quiz.questions.*
import com.fim.prototype.mish.services.chapters.ModelService
import com.fim.prototype.mish.services.quiz.CreateQuizValidator
import org.springframework.stereotype.Service
import java.util.*
import kotlin.reflect.KClass

@Service
class MultipleChoiceQuestionValidator(
    val questionValidatorUtils: QuestionValidatorUtils
) : CreateQuizValidator {
    override val type: KClass<*>
        get() = MultipleChoiceQuestionData::class

    override fun validate(question: AbstractQuestionData, answer: AbstractAnswerData): Pair<AbstractQuestionData, AbstractAnswerData> {
        return questionValidatorUtils.validate(question, answer) { q, a ->
            val typedQuestion = q.asMultipleChoiceQuestionData()

            if (typedQuestion.options.size <= 2) throw ValidationException("Multiple choice question must have at least two options!")

            val typedAnswer = a.asMultipleChoiceAnswerData()

            q to a
        }
    }
}

@Service
class MatchingQuestionValidator(
    val questionValidatorUtils: QuestionValidatorUtils
) : CreateQuizValidator {
    override val type: KClass<*>
        get() = MatchingQuestionData::class

    override fun validate(question: AbstractQuestionData, answer: AbstractAnswerData): Pair<AbstractQuestionData, AbstractAnswerData> {
        return questionValidatorUtils.validate(question, answer) { q, a ->
            val typedQuestion = q.asMatchingQuestionData()

            if (typedQuestion.leftItems.size <= 2) throw ValidationException("Left items in question must have at least two left item!")
            if (typedQuestion.rightItems.size <= 2) throw ValidationException("Right items in question must have at least two right item!")

            val typedAnswer = a.asMatchingAnswerData()

            q to a
        }
    }
}

@Service
class OpenTextQuestionValidator(
    val questionValidatorUtils: QuestionValidatorUtils
) : CreateQuizValidator {
    override val type: KClass<*>
        get() = OpenTextQuestionData::class

    override fun validate(question: AbstractQuestionData, answer: AbstractAnswerData): Pair<AbstractQuestionData, AbstractAnswerData> {
        return questionValidatorUtils.validate(question, answer) { q, a -> q to a}
    }
}

@Service
class OrderingQuestionValidator(
    val questionValidatorUtils: QuestionValidatorUtils
) : CreateQuizValidator {
    override val type: KClass<*>
        get() = OrderingQuestionData::class

    override fun validate(question: AbstractQuestionData, answer: AbstractAnswerData): Pair<AbstractQuestionData, AbstractAnswerData> {
        return questionValidatorUtils.validate(question, answer) { q, a ->
            val typedQuestion = q.asOrderingQuestionData()

            if (typedQuestion.items.size <= 2) throw ValidationException("Ordering items in question must have at least two item!")

            val typedAnswer = a.asOrderingAnswerData()

            q to a
        }
    }
}

@Service
class SingleChoiceQuestionValidator(
    val questionValidatorUtils: QuestionValidatorUtils
) : CreateQuizValidator {
    override val type: KClass<*>
        get() = SingleChoiceQuestionData::class

    override fun validate(question: AbstractQuestionData, answer: AbstractAnswerData): Pair<AbstractQuestionData, AbstractAnswerData> {
        return questionValidatorUtils.validate(question, answer) { q, a ->
            val typedQuestion = q.asSingleChoiceQuestionData()

            if (typedQuestion.options.size <= 2) throw ValidationException("Single choice question must have at least two options!")

            val typedAnswer = a.asSingleChoiceAnswerData()

            q to a
        }
    }
}

@Service
class TextureClickQuestionValidator(
    val questionValidatorUtils: QuestionValidatorUtils,
    val modelService: ModelService,
) : CreateQuizValidator {
    override val type: KClass<*>
        get() = TextureClickQuestionData::class

    override fun validate(question: AbstractQuestionData, answer: AbstractAnswerData): Pair<AbstractQuestionData, AbstractAnswerData> {
        return questionValidatorUtils.validate(question, answer) { q, a ->
            val typedQuestion = question.asTextureClickQuestionData()

            val modelExists = typedQuestion.modelId?.let { id -> modelService.isFileExists(id) }?: false
            val textureExists = typedQuestion.textureId?.let { id -> modelService.isFileExists(id) }?: false

            if (!modelExists) throw NotFoundException("Model for question not found!")
            if (!textureExists) throw NotFoundException("Texture for question not found!")

            val typedAnswer = answer.asTextureClickAnswerData()

            q to a
        }
    }
}

@Service
class QuestionValidatorUtils {

    fun validate(
        question: AbstractQuestionData,
        answer: AbstractAnswerData,
        validate: (AbstractQuestionData, AbstractAnswerData) -> Pair<AbstractQuestionData, AbstractAnswerData> = { q, a -> q to a }
    ): Pair<AbstractQuestionData, AbstractAnswerData> {
        question.questionId = UUID.randomUUID().toString()
        if (question.points <= 0) throw ValidationException("Question points must be greater than 0!")
        if (question.questionText.isEmpty()) throw ValidationException("Question text must not be empty!")
        if (question.type?.supportedClazz?.contains(question::class) == false) throw ValidationException("Question type is not supported by class type!")

        return validate(question, answer)
    }
}
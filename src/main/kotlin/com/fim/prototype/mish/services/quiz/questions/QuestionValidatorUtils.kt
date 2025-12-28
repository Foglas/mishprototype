package com.fim.prototype.mish.services.quiz.questions

import com.fim.prototype.mish.exceptions.NotFoundException
import com.fim.prototype.mish.exceptions.ValidationException
import com.fim.prototype.mish.model.entities.quiz.QuestionType
import com.fim.prototype.mish.model.entities.quiz.questions.*
import com.fim.prototype.mish.services.chapters.ModelService
import org.springframework.stereotype.Service
import java.util.*
import kotlin.reflect.KClass

@Service
class MultipleChoiceQuestionValidator(
    val questionValidatorUtils: QuestionValidatorUtils
) : QuestionValidator {
    override val type: KClass<*>
        get() = MultipleChoiceQuestionData::class

    override fun validate(question: AbstractQuestionData): AbstractQuestionData {
        return questionValidatorUtils.validate(question) {
            val typedQuestion = it.asMultipleChoiceQuestionData()

            if (typedQuestion.options.size >= 2) throw ValidationException("Multiple choice question must have at least two options!")
            it
        }
    }
}

@Service
class MatchingQuestionValidator(
    val questionValidatorUtils: QuestionValidatorUtils
) : QuestionValidator {
    override val type: KClass<*>
        get() = MatchingQuestionData::class

    override fun validate(question: AbstractQuestionData): AbstractQuestionData {
        return questionValidatorUtils.validate(question) {
            val typedQuestion = it.asMatchingQuestionData()

            if (typedQuestion.leftItems.size >= 2) throw ValidationException("Left items in question must have at least two left item!")
            if (typedQuestion.rightItems.size >= 2) throw ValidationException("Right items in question must have at least two right item!")

            it
        }
    }
}

@Service
class OpenTextQuestionValidator(
    val questionValidatorUtils: QuestionValidatorUtils
) : QuestionValidator {
    override val type: KClass<*>
        get() = OpenTextQuestionData::class

    override fun validate(question: AbstractQuestionData): AbstractQuestionData {
        return questionValidatorUtils.validate(question) {it}
    }
}

@Service
class OrderingQuestionValidator(
    val questionValidatorUtils: QuestionValidatorUtils
) : QuestionValidator {
    override val type: KClass<*>
        get() = OrderingQuestionData::class

    override fun validate(question: AbstractQuestionData): AbstractQuestionData {
        return questionValidatorUtils.validate(question) {
            val typedQuestion = it.asOrderingQuestionData()

            if (typedQuestion.items.size >= 2) throw ValidationException("Ordering items in question must have at least two item!")

            it
        }
    }
}

@Service
class SingleChoiceQuestionValidator(
    val questionValidatorUtils: QuestionValidatorUtils
) : QuestionValidator {
    override val type: KClass<*>
        get() = SingleChoiceQuestionData::class

    override fun validate(question: AbstractQuestionData): AbstractQuestionData {
        return questionValidatorUtils.validate(question) {
            val typedQuestion = it.asSingleChoiceQuestionData()

            if (typedQuestion.options.size >= 2) throw ValidationException("Single choice question must have at least two options!")
            it
        }
    }
}

@Service
class TextureClickQuestionValidator(
    val questionValidatorUtils: QuestionValidatorUtils,
    val modelService: ModelService,
) : QuestionValidator {
    override val type: KClass<*>
        get() = TextureClickQuestionData::class

    override fun validate(question: AbstractQuestionData): AbstractQuestionData {
        return questionValidatorUtils.validate(question) {
            val typedQuestion = it.asTextureClickQuestionData()

            val modelExists = typedQuestion.modelId?.let { id -> modelService.isFileExists(id) }?: false
            val textureExists = typedQuestion.textureId?.let { id -> modelService.isFileExists(id) }?: false

            if (!modelExists) throw NotFoundException("Model for question not found!")
            if (!textureExists) throw NotFoundException("Texture for question not found!")

            it
        }
    }
}

@Service
class QuestionValidatorUtils {

    fun validate(
        question: AbstractQuestionData,
        validate: (AbstractQuestionData) -> AbstractQuestionData = { it }
    ): AbstractQuestionData {
        question.questionId = UUID.randomUUID().toString()
        if (question.points <= 0) throw ValidationException("Question points must be greater than 0!")
        if (question.questionText.isEmpty()) throw ValidationException("Question text must not be empty!")
        if (question.type?.supportedClazz?.contains(QuestionType::class) == false) throw ValidationException("Question type is not supported by class type!")

        return validate(question)
    }
}
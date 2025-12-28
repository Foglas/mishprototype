package com.fim.prototype.mish.model.entities.quiz.answers

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fim.prototype.mish.model.entities.quiz.QuestionType

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "_class"
)
@JsonSubTypes(
    JsonSubTypes.Type(MatchingAnswerData::class, name = "MatchingAnswerData"),
    JsonSubTypes.Type(OrderingAnswerData::class, name = "OrderingAnswerData"),
    JsonSubTypes.Type(SingleChoiceAnswerData::class, name = "SingleChoiceAnswerData"),
    JsonSubTypes.Type(OpenTextAnswerData::class, name = "OpenTextAnswerData"),
    JsonSubTypes.Type(MultipleChoiceAnswerData::class, name = "MultipleChoiceAnswerData"),
    JsonSubTypes.Type(TextureClickAnswerData::class, name = "TextureClickAnswerData")
)
sealed class AbstractAnswerData {
    var questionId: String? = null
    var type: QuestionType? = null
}

fun AbstractAnswerData.asMatchingAnswerData(): MatchingAnswerData {
    if (this !is MatchingAnswerData) {
        throw IllegalStateException("Answer data is not of type MatchingAnswerData")
    }
    return this
}

fun AbstractAnswerData.asOrderingAnswerData(): OrderingAnswerData {
    if (this !is OrderingAnswerData) {
        throw IllegalStateException("Answer data is not of type OrderingAnswerData")
    }
    return this
}

fun AbstractAnswerData.asSingleChoiceAnswerData(): SingleChoiceAnswerData {
    if (this !is SingleChoiceAnswerData) {
        throw IllegalStateException("Answer data is not of type SingleChoiceAnswerData")
    }
    return this
}

fun AbstractAnswerData.asOpenTextAnswerData(): OpenTextAnswerData {
    if (this !is OpenTextAnswerData) {
        throw IllegalStateException("Answer data is not of type OpenTextAnswerData")
    }
    return this
}

fun AbstractAnswerData.asMultipleChoiceAnswerData(): MultipleChoiceAnswerData {
    if (this !is MultipleChoiceAnswerData) {
        throw IllegalStateException("Answer data is not of type MultipleChoiceAnswerData")
    }
    return this
}

fun AbstractAnswerData.asTextureClickAnswerData(): TextureClickAnswerData {
    if (this !is TextureClickAnswerData) {
        throw IllegalStateException("Answer data is not of type TextureClickAnswerData")
    }
    return this
}

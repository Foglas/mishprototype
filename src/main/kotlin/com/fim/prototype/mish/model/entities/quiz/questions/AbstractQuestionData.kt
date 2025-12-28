package com.fim.prototype.mish.model.entities.quiz.questions

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fim.prototype.mish.model.entities.quiz.QuestionType

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "_class"
)
@JsonSubTypes(
    JsonSubTypes.Type(MultipleChoiceQuestionData::class, name = "MultipleChoiceQuestionData"),
    JsonSubTypes.Type(SingleChoiceQuestionData::class, name = "SingleChoiceQuestionData"),
    JsonSubTypes.Type(OpenTextQuestionData::class, name = "OpenTextQuestionData"),
    JsonSubTypes.Type(MatchingQuestionData::class, name = "MatchingQuestionData"),
    JsonSubTypes.Type(OrderingQuestionData::class, name = "OrderingQuestionData"),
    JsonSubTypes.Type(TextureClickQuestionData::class, name = "TextureClickQuestionData")
)
abstract class AbstractQuestionData {
    var questionId: String? = null
    var questionText: String = ""
    var type: QuestionType? = null
    var points: Int = 0
}


fun AbstractQuestionData.asMatchingQuestionData(): MatchingQuestionData {
    if (this !is MatchingQuestionData) {
        throw IllegalStateException("Question is not of type MatchingQuestionData")
    }
    return this
}

fun AbstractQuestionData.asOrderingQuestionData(): OrderingQuestionData {
    if (this !is OrderingQuestionData) {
        throw IllegalStateException("Question is not of type OrderingQuestionData")
    }
    return this
}

fun AbstractQuestionData.asTextureClickQuestionData(): TextureClickQuestionData {
    if (this !is TextureClickQuestionData) {
        throw IllegalStateException("Question is not of type TextureClickQuestionData")
    }
    return this
}

fun AbstractQuestionData.asMultipleChoiceQuestionData(): MultipleChoiceQuestionData {
    if (this !is MultipleChoiceQuestionData) {
        throw IllegalStateException("Question is not of type MultipleChoiceQuestionData")
    }
    return this
}

fun AbstractQuestionData.asSingleChoiceQuestionData(): SingleChoiceQuestionData {
    if (this !is SingleChoiceQuestionData) {
        throw IllegalStateException("Question is not of type SingleChoiceQuestionData")
    }
    return this
}

fun AbstractQuestionData.asOpenTextQuestionData(): OpenTextQuestionData {
    if (this !is OpenTextQuestionData) {
        throw IllegalStateException("Question is not of type OpenTextQuestionData")
    }
    return this
}

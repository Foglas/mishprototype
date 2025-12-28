package com.fim.prototype.mish.model.entities.quiz.submission

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fim.prototype.mish.model.entities.quiz.QuestionType

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "_class"
)
@JsonSubTypes(
    JsonSubTypes.Type(MultipleChoiceSubmissionData::class, name = "MultipleChoiceSubmissionData"),
    JsonSubTypes.Type(SingleChoiceSubmissionData::class, name = "SingleChoiceSubmissionData"),
    JsonSubTypes.Type(OpenTextSubmissionData::class, name = "OpenTextSubmissionData"),
    JsonSubTypes.Type(MatchingSubmissionData::class, name = "MatchingSubmissionData"),
    JsonSubTypes.Type(OrderingSubmissionData::class, name = "OrderingSubmissionData"),
    JsonSubTypes.Type(TextureClickSubmissionData::class, name = "TextureClickSubmissionData")
)
sealed class AbstractSubmissionData {
    var questionId: String? = null
    var type: QuestionType? = null
}


fun AbstractSubmissionData.asMatchingSubmissionData(): MatchingSubmissionData {
    if (this !is MatchingSubmissionData) {
        throw IllegalStateException("Submission data is not of type MatchingSubmissionData")
    }
    return this
}

fun AbstractSubmissionData.asOrderingSubmissionData(): OrderingSubmissionData {
    if (this !is OrderingSubmissionData) {
        throw IllegalStateException("Submission data is not of type OrderingSubmissionData")
    }
    return this
}

fun AbstractSubmissionData.asTextureClickSubmissionData(): TextureClickSubmissionData {
    if (this !is TextureClickSubmissionData) {
        throw IllegalStateException("Submission data is not of type TextureClickSubmissionData")
    }
    return this
}

fun AbstractSubmissionData.asMultipleChoiceSubmissionData(): MultipleChoiceSubmissionData {
    if (this !is MultipleChoiceSubmissionData) {
        throw IllegalStateException("Submission data is not of type MultipleChoiceSubmissionData")
    }
    return this
}

fun AbstractSubmissionData.asSingleChoiceSubmissionData(): SingleChoiceSubmissionData {
    if (this !is SingleChoiceSubmissionData) {
        throw IllegalStateException("Submission data is not of type SingleChoiceSubmissionData")
    }
    return this
}

fun AbstractSubmissionData.asOpenTextSubmissionData(): OpenTextSubmissionData {
    if (this !is OpenTextSubmissionData) {
        throw IllegalStateException("Submission data is not of type OpenTextSubmissionData")
    }
    return this
}
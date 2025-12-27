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
abstract class AbstractSubmissionData {
    var questionId: String? = null
    var type: QuestionType? = null
}

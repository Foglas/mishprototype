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

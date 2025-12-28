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
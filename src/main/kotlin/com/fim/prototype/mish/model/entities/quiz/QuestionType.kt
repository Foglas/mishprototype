package com.fim.prototype.mish.model.entities.quiz

import com.fim.prototype.mish.model.entities.quiz.answers.*
import com.fim.prototype.mish.model.entities.quiz.questions.*
import com.fim.prototype.mish.model.entities.quiz.submission.*
import kotlin.reflect.KClass

enum class QuestionType(val supportedClazz: Set<KClass<*>>) {
    MULTIPLE_CHOICE(setOf(MultipleChoiceQuestionData::class, MultipleChoiceAnswerData::class, MultipleChoiceSubmissionData::class)),
    SINGLE_CHOICE(setOf(SingleChoiceQuestionData::class, SingleChoiceAnswerData::class, SingleChoiceSubmissionData::class)),
    OPEN_TEXT(setOf(OpenTextQuestionData::class, OpenTextAnswerData::class, OpenTextSubmissionData::class)),
    MATCHING(setOf(MatchingQuestionData::class, MatchingAnswerData::class, MatchingSubmissionData::class)),
    ORDERING(setOf(OrderingQuestionData::class, OrderingAnswerData::class, OrderingSubmissionData::class)),
    TEXTURE_CLICK(setOf(TextureClickQuestionData::class, TextureClickAnswerData::class, TextureClickSubmissionData::class)),
}
package com.fim.prototype.mish.model.entities.quiz

import com.fim.prototype.mish.model.entities.abstracts.AbstractEntity
import com.fim.prototype.mish.model.entities.quiz.answers.AbstractAnswerData
import com.fim.prototype.mish.model.entities.quiz.questions.AbstractQuestionData
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "quiz")
data class QuizEntity(
    @Id
    override var id: String?,
    var questions: List<AbstractQuestionData> = listOf(),
    var answers: List<AbstractAnswerData> = listOf(),
   ) : QuickQuizEntity()

open class QuickQuizEntity : AbstractEntity() {
    open var chapterId: String? = null
    open var timeLimit: Int? = null
}
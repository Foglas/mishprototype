package com.fim.prototype.mish.model.entities.quiz

import com.fim.prototype.mish.model.entities.abstracts.AbstractEntity
import com.fim.prototype.mish.model.entities.quiz.answers.AbstractAnswerData
import com.fim.prototype.mish.model.entities.quiz.questions.AbstractQuestionData
import com.fim.prototype.mish.repo.MongoCollection
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = MongoCollection.QUIZ_ENTITY)
data class QuizEntity(
    @Id
    override var id: String?,
    var questions: List<AbstractQuestionData> = listOf(),
    var answers: List<AbstractAnswerData> = listOf(),
   ) : QuickQuizEntity() {
       var maxScore = questions.sumOf { it.points }
   }

open class QuickQuizEntity : AbstractEntity() {
    open var chapterId: String? = null
    open var timeLimit: Int = 0
}
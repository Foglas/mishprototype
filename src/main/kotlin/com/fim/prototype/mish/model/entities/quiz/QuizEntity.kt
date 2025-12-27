package com.fim.prototype.mish.model.entities.quiz

import com.fim.prototype.mish.model.entities.abstracts.AbstractEntity
import com.fim.prototype.mish.model.entities.quiz.answers.AbstractAnswerData
import com.fim.prototype.mish.model.entities.quiz.questions.AbstractQuestionData
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "quiz")
data class QuizEntity(
    override var id: String?,
    var questions: List<AbstractQuestionData> = listOf(),
    var answers: List<AbstractAnswerData> = listOf(),
    override var name: String? = null,
    override var description: String? = null,
    override var chapterId: String? = null,
    override var creatorId: String? = null,
    override var timeLimit: Int? = null,
    override var created: Instant = Instant.now(),
    override var updated: Instant = Instant.now(),
) : QuickQuizEntity()

open class QuickQuizEntity : AbstractEntity() {
    open var chapterId: String? = null
    open var timeLimit: Int? = null
}
package com.fim.prototype.mish.model.entities.quiz

import com.fim.prototype.mish.model.entities.abstracts.AbstractEntity
import com.fim.prototype.mish.model.entities.quiz.answers.AbstractAnswerData
import com.fim.prototype.mish.model.entities.quiz.questions.AbstractQuestionData
import com.fim.prototype.mish.repo.MongoCollection
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = MongoCollection.QUIZ_ENTITY)
data class QuizEntity(
    var questions: List<AbstractQuestionData> = listOf(),
    var answers: List<AbstractAnswerData> = listOf()
) : QuickQuizEntity() {
    val maxScore: Int
        get() = questions.sumOf { it.points }
}

open class QuickQuizEntity(
    override var id: String? = null,
    open var chapterId: String? = null,
    open var timeLimit: Int = 0,
    override var name: String = "",
    override var creatorId: String? = null,
    override var description: String? = null,
    override var created: Instant? = Instant.now(),
    override var updated: Instant? = Instant.now()
) : AbstractEntity()

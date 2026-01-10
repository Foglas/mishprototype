package com.fim.prototype.mish.model.entities.quiz

import com.fim.prototype.mish.model.entities.quiz.answers.AbstractAnswerData
import com.fim.prototype.mish.model.entities.quiz.questions.AbstractQuestionData
import com.fim.prototype.mish.repo.MongoCollection
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = MongoCollection.QUIZ_ENTITY)
data class QuizEntity(
    var id: String? = null,
    var chapterId: String? = null,
    var timeLimit: Int = 0,
    var name: String = "",
    var creatorId: String? = null,
    var description: String? = null,
    var questions: List<AbstractQuestionData> = listOf(),
    var answers: List<AbstractAnswerData> = listOf(),
    var created: Instant? = Instant.now(),
    var updated: Instant? = Instant.now()

) {
    val maxScore: Int
        get() = questions.sumOf { it.points }
}
 
class QuickQuizEntity(
    var id: String? = null,
    var chapterId: String? = null,
    var timeLimit: Int = 0,
    var name: String = "",
    var creatorId: String? = null,
    var description: String? = null,
    var created: Instant? = Instant.now(),
    var updated: Instant? = Instant.now()
)

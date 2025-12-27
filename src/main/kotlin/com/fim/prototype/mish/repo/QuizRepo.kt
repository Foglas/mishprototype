package com.fim.prototype.mish.repo

import com.fim.prototype.mish.model.entities.quiz.QuizEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface QuizRepo : MongoRepository<QuizEntity, String> {
}
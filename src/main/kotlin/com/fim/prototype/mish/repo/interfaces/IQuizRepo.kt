package com.fim.prototype.mish.repo.interfaces

import com.fim.prototype.mish.model.entities.quiz.QuizEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface IQuizRepo : MongoRepository<QuizEntity, String>
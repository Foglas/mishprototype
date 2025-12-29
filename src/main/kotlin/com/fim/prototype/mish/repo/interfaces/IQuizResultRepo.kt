package com.fim.prototype.mish.repo.interfaces

import com.fim.prototype.mish.model.entities.quiz.QuizValidationResultWithUser
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface IQuizResultRepo : MongoRepository<QuizValidationResultWithUser, String>
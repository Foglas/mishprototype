package com.fim.prototype.mish.services

import com.fim.prototype.mish.exceptions.InternalServerError
import com.fim.prototype.mish.exceptions.NotFoundException
import com.fim.prototype.mish.model.entities.quiz.QuizSubmissionRequest
import com.fim.prototype.mish.model.entities.quiz.QuizValidationResult
import com.fim.prototype.mish.repo.QuizRepo
import org.springframework.stereotype.Service

@Service
class QuizValidatorService(
    private val quizRepo: QuizRepo,
    quizValidators: Set<QuizValidator>
) {

    val validatorsMap = quizValidators.associateBy { it.type }

    fun validateAnswers(quizId: String, submission: QuizSubmissionRequest): QuizValidationResult {
        val quizAnswers = quizRepo.getQuizById(quizId, true)?.answers
            ?.associateBy { it.questionId }
            ?: throw NotFoundException("Quiz answers was not found")

        submission.answers.forEach { submitted ->
            val correctAnswer = quizAnswers[submitted.questionId] ?: throw NotFoundException("Submitted answer was not found in quiz answers!")
            validatorsMap[correctAnswer::class]?.validate(correctAnswer, correctAnswer) ?: throw InternalServerError("Validator for answer type ${correctAnswer::class} was not found!")
        }

        return QuizValidationResult()
    }
}
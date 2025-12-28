package com.fim.prototype.mish.services

import com.fim.prototype.mish.exceptions.InternalServerError
import com.fim.prototype.mish.exceptions.NotFoundException
import com.fim.prototype.mish.model.common.QuestionPartValidation
import com.fim.prototype.mish.model.entities.quiz.QuizSubmissionRequest
import com.fim.prototype.mish.model.entities.quiz.QuizValidationResult
import com.fim.prototype.mish.repo.QuizRepo
import org.springframework.stereotype.Service

@Service
class QuizValidatorService(
    private val quizRepo: QuizRepo,
    private val statsService: StatsService,
    quizValidators: Set<QuizValidator>
) {

    val validatorsMap = quizValidators.associateBy { it.type }

    fun validateAnswers(quizId: String, submission: QuizSubmissionRequest): QuizValidationResult {
        val quiz = quizRepo.getQuizById(quizId, true)
            ?: throw NotFoundException("Quiz with id $quizId not found!")

        val quizAnswers = quiz.answers.associateBy { it.questionId }
        val quizQuestions = quiz.questions.associateBy { it.questionId }

        val answersResult = submission.answers.map { submitted ->
            val correctAnswer = quizAnswers[submitted.questionId]
                ?: throw NotFoundException("Submitted answer was not found in quiz answers!")

            val quizQuestion = quizQuestions[submitted.questionId]
                ?: throw NotFoundException("Question for submitted answer was not found in quiz questions!")

            val validationResult = validatorsMap[correctAnswer::class]?.validate(correctAnswer, submitted)
                ?: throw InternalServerError("Validator for answer type ${correctAnswer::class} was not found!")


            QuestionPartValidation(
                validationResult,
                quizQuestion.points,
                quizQuestion.questionText
            )
        }

        val totalScore = answersResult.sumOf { it.points }

        return QuizValidationResult(
            totalScore = totalScore,
            maxScore = quiz.maxScore,
            percentage = statsService.calculatePercentage(totalScore, quiz.maxScore),
            questionResults = answersResult.associate { it.text to it.isCorrect },
            questionScores = answersResult.associate { it.text to it.points }
        )
    }
}
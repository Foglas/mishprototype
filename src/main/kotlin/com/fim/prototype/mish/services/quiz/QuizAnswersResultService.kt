package com.fim.prototype.mish.services.quiz

import com.fim.prototype.mish.exceptions.InternalServerError
import com.fim.prototype.mish.exceptions.NotFoundException
import com.fim.prototype.mish.model.common.QuestionPartValidation
import com.fim.prototype.mish.model.entities.quiz.QuizSubmissionRequest
import com.fim.prototype.mish.model.entities.quiz.QuizValidationQuestion
import com.fim.prototype.mish.model.entities.quiz.QuizValidationResult
import com.fim.prototype.mish.repo.QuizRepo
import com.fim.prototype.mish.services.StatsService
import com.fim.prototype.mish.services.quiz.validators.QuizResultValidator
import org.springframework.stereotype.Service

@Service
class QuizAnswersResultService(
    private val quizRepo: QuizRepo,
    private val statsService: StatsService,
    quizResultValidators: Set<QuizResultValidator>
) {

    val validatorsMap = quizResultValidators.associateBy { it.type }

    fun getAnswersResult(quizId: String, submission: QuizSubmissionRequest): QuizValidationResult {
        val quiz = quizRepo.getQuizById(quizId, true)
            ?: throw NotFoundException("Quiz with id $quizId not found!")

        //TODO TEST: handle validation of missing, duplicate or extra answers in submission
        val quizAnswers = quiz.answers.distinctBy { it.questionId }.associateBy { it.questionId }
        val quizQuestions =quiz.questions.distinctBy { it.questionId }.associateBy { it.questionId }

        val answersResult = submission.answers.mapNotNull { submitted ->
            val quizQuestion = quizQuestions[submitted.questionId]
                ?: return@mapNotNull null

            val correctAnswer = quizAnswers[submitted.questionId]
                ?: throw NotFoundException("Submitted answer was not found in quiz answers!")

            val validationResult = validatorsMap[correctAnswer::class]?.validate(correctAnswer, submitted)
                ?: throw InternalServerError("Validator for answer type ${correctAnswer::class} was not found!")

            QuestionPartValidation(
                quizQuestion.questionId,
                validationResult,
                if (validationResult) quizQuestion.points else 0,
                quizQuestion.questionText,
                submitted
            )
        }

        val answeredIds = answersResult.mapTo(mutableSetOf()) { it.questionId }

        val completedResults = answersResult + quizQuestions.values
            .filter { it.questionId !in answeredIds }
            .map { question ->
                QuestionPartValidation(
                    questionId = question.questionId,
                    isCorrect = false,
                    points = 0,
                    text = question.questionText,
                )
            }

        val totalScore = completedResults
            .asSequence()
            .filter { it.isCorrect }
            .sumOf { it.points }

        return QuizValidationResult(
            quizId = quizId,
            name = quiz.name,
            totalScore = totalScore,
            maxScore = quiz.maxScore,
            percentage = statsService.calculatePercentage(totalScore, quiz.maxScore),
            questionResults = answersResult.map { QuizValidationQuestion(
                questionText = it.text,
                isCorrect = it.isCorrect,
                points = it.points,
                it.submission
            ) },
        )
    }
}
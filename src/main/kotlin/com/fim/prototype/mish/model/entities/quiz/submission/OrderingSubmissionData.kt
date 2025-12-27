package com.fim.prototype.mish.model.entities.quiz.submission

data class OrderingSubmissionData(
    var order: List<Int> = listOf()
) : AbstractSubmissionData()
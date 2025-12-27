package com.fim.prototype.mish.model.entities.quiz.submission

data class MatchingSubmissionData(
    var matches: Map<Int, Int> = mapOf()
) : AbstractSubmissionData()
package com.fim.prototype.mish.model.entities.quiz.submission

data class MultipleChoiceSubmissionData(
    var selectedItems: List<Int> = listOf()
) : AbstractSubmissionData()

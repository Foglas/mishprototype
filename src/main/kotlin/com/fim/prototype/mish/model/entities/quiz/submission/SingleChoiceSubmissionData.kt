package com.fim.prototype.mish.model.entities.quiz.submission

data class SingleChoiceSubmissionData(
    var selectedIndex: Int? = null
) : AbstractSubmissionData()

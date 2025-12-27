package com.fim.prototype.mish.model.entities.quiz.submission

data class OpenTextSubmissionData(
    var text: String = ""
) : AbstractSubmissionData()
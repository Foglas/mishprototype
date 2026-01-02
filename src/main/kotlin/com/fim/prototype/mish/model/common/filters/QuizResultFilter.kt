package com.fim.prototype.mish.model.common.filters

import java.time.Instant

data class QuizResultFilter(
    val quizId: String? = null,
    override var name: String? = null,
    override var creatorId: String? = null,
    override var createdFrom: Instant? = null,
    override var createdTo: Instant? = null,
): FilterBase()
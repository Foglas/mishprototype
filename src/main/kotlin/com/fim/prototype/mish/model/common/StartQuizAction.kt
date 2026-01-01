package com.fim.prototype.mish.model.common

import java.time.Instant

data class StartQuizAction(
    val hasTimeLimit: Boolean,
    val time: Instant = Instant.now(),
)

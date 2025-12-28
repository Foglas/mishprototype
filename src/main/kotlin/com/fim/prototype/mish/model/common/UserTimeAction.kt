package com.fim.prototype.mish.model.common

import java.time.Instant

data class UserTimeAction<T>(
    val userId: String,
    val timestamp: Instant = Instant.now(),
    val data: T
)

package com.fim.prototype.mish.services

import org.springframework.stereotype.Service

@Service
class StatsService {

    fun calculatePercentage(part: Int, total: Int): Double{
        return if (total == 0) 0.0 else (part.toDouble() / total.toDouble()) * 100.0
    }
}
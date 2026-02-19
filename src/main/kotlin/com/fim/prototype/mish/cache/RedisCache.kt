package com.fim.prototype.mish.cache

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

@Service
class RedisCache(
    private val redisTemplate: RedisTemplate<String, Any>,
): ICache {

    override fun put(key: String, value: Any, ttlSeconds: Long?) {
        if (ttlSeconds != null) {
            redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(ttlSeconds))
        } else {
            redisTemplate.opsForValue().set(key, value)
        }
    }

    override fun <T : Any> get(key: String, type: KClass<T>): T? {
        val value = redisTemplate.opsForValue().get(key)
        return type.safeCast(value)
    }

    override fun <T : Any> delete(key: String, type: KClass<T>): T? {
        val value = redisTemplate.opsForValue().get(key)
        redisTemplate.delete(key)
        return type.safeCast(value)
    }
}
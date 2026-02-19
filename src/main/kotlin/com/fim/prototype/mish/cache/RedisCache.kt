package com.fim.prototype.mish.cache

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

@Service
class RedisCache(
    private val redisTemplate: RedisTemplate<String, String>,
    private val objectMapper: ObjectMapper
): ICache {

    override fun put(key: String, value: Any, ttlSeconds: Long?) {
        if (ttlSeconds != null) {
            redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(value), Duration.ofSeconds(ttlSeconds))
        } else {
            redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(value))
        }
    }

    override fun <T : Any> get(key: String, type: KClass<T>): T? {
        val value = redisTemplate.opsForValue().get(key)
        return type.safeCast(value)
    }

    override fun <T : Any> delete(key: String, type: Class<T>): T? {
        val json = redisTemplate.opsForValue().get(key) ?: return null
        redisTemplate.delete(key)
        val javaType = objectMapper.typeFactory.constructType(type)
        return objectMapper.readValue(json, javaType)
    }
}
package com.fim.prototype.mish.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisClusterConfiguration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {


    @Bean
    fun redisConnectionFactory(properties: RedisProperties): RedisConnectionFactory {
        val clusterNodes = properties.cluster?.nodes ?: emptyList()
        val clusterConfig = RedisClusterConfiguration(clusterNodes)

        if (!properties.password.isNullOrEmpty()) {
            clusterConfig.setPassword(RedisPassword.of(properties.password))
        }

        return LettuceConnectionFactory(clusterConfig)
    }

    @Bean
    fun redisTemplate(properties: RedisProperties): RedisTemplate<String, Any> {
        val mapper = ObjectMapper()
            .registerModule(JavaTimeModule()) // handle Instant, LocalDate, etc.
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // optional: use ISO-8601 format

        val serializer = GenericJackson2JsonRedisSerializer(mapper)

        return RedisTemplate<String, Any>().apply {
            connectionFactory = redisConnectionFactory(properties)
            keySerializer = StringRedisSerializer()
            valueSerializer = serializer
            hashKeySerializer = StringRedisSerializer()
            hashValueSerializer = serializer
            afterPropertiesSet()
        }
    }
}
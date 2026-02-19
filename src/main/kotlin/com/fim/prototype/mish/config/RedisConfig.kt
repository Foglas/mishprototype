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
        return if (!properties.cluster?.nodes.isNullOrEmpty()) {
            val clusterConfig = RedisClusterConfiguration(properties.cluster!!.nodes)
            if (!properties.username.isNullOrEmpty() || !properties.password.isNullOrEmpty()) {
                clusterConfig.setUsername(properties.username)       // <-- add this
                clusterConfig.setPassword(RedisPassword.of(properties.password))
            }
            LettuceConnectionFactory(clusterConfig)
        } else {
            val factory = LettuceConnectionFactory(
                properties.host,
                properties.port
            )
            if (!properties.username.isNullOrEmpty() || !properties.password.isNullOrEmpty()) {
                factory.setPassword(properties.password)
            }
            factory.afterPropertiesSet()
            factory
        }
    }

    @Bean
    fun redisTemplate(properties: RedisProperties): RedisTemplate<String, String> {
        return RedisTemplate<String, String>().apply {
            connectionFactory = redisConnectionFactory(properties)
            keySerializer = StringRedisSerializer()
            valueSerializer = StringRedisSerializer()
            hashKeySerializer = StringRedisSerializer()
            hashValueSerializer = StringRedisSerializer()
            afterPropertiesSet()
        }
    }
}
package com.fim.prototype.mish.cache

import kotlin.reflect.KClass

interface ICache {
    fun put(key: String, value: Any, ttlSeconds: Long? = null)
    fun <T : Any> get(key: String, type: KClass<T>): T?
    fun <T : Any> delete(key: String, type: Class<T>): T?
}
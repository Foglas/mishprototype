package com.fim.prototype.mish.cache

interface ICache<K,V> {
    fun put(key: K, value: V, ttlSeconds: Long? = null)
    fun delete(key: K): V?
    fun get(key: K): V?
}
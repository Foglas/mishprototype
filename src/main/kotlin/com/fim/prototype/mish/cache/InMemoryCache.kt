package com.fim.prototype.mish.cache

import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class InMemoryCache<K,V> : ICache<K,V> {

    private val cache: MutableMap<K,V> = ConcurrentHashMap()

    override fun put(key: K, value: V, ttlSeconds: Long?) {
        cache[key] = value
    }

    override fun delete(key: K): V? {
        return cache.remove(key)
    }

    override fun get(key: K): V? {
        return cache[key]
    }

}
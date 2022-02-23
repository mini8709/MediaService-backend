package com.mediaservice.infrastructure

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class RedisUtil(
    private val stringRedisTemplate: StringRedisTemplate
) {
    fun getData(key: String): String? {
        val valueOperations = stringRedisTemplate.opsForValue()
        return valueOperations[key]
    }

    fun setData(key: String, value: String): String {
        val valueOperations = stringRedisTemplate.opsForValue()
        valueOperations[key] = value
        return value
    }

    fun setDataExpire(key: String, value: String, duration: Long): String {
        val valueOperations = stringRedisTemplate.opsForValue()
        val expireDuration = Duration.ofSeconds(duration)
        valueOperations[key, value] = expireDuration
        return value
    }

    fun deleteData(key: String) {
        stringRedisTemplate.delete(key)
    }
}

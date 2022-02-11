package com.mediaservice.domain

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash(value = "refreshToken")
data class RefreshToken(
    @Id
    val id: String?
)

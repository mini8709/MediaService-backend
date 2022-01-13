package com.mediaservice.application.dto.user

import com.mediaservice.domain.User
import java.util.UUID

data class UserResponseDto(
    val id: UUID,
    val email: String
) {
    companion object {
        fun from(user: User) = UserResponseDto(
            id = user.id!!,
            email = user.email
        )
    }
}

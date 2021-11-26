package com.mediaservice.application

import com.mediaservice.application.dto.UserResponseDto
import com.mediaservice.domain.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.Exception
import java.util.*

@Service
class UserService(private val userRepository: UserRepository) {
    @Transactional(readOnly = true)
    fun findById(id: UUID): UserResponseDto {
        return UserResponseDto.from(
            this.userRepository.findById(id)?: throw Exception()
        )
    }
}
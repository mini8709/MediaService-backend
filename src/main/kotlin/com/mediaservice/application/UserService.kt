package com.mediaservice.application

import com.mediaservice.application.dto.UserRequestDto
import com.mediaservice.application.dto.UserResponseDto
import com.mediaservice.config.JwtTokenProvider
import com.mediaservice.domain.repository.UserRepository
import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.ErrorCode
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: JwtTokenProvider
) {
    @Transactional(readOnly = true)
    fun findById(id: UUID): UserResponseDto {
        return UserResponseDto.from(
            this.userRepository.findById(id) ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST, "NO SUCH PROFILE $id"
            )
        )
    }

    @Transactional
    fun signUp(userRequestDto: UserRequestDto): UserResponseDto {
        if (this.userRepository.findByEmail(userRequestDto.email) != null) {
            throw BadRequestException(ErrorCode.ROW_ALREADY_EXIST, "DUPLICATE_EMAIL")
        }
        return UserResponseDto.from(
            this.userRepository.save(
                userRequestDto.email,
                passwordEncoder.encode(userRequestDto.password)
            )
        )
    }

    @Transactional(readOnly = true)
    fun signIn(userRequestDto: UserRequestDto): String {
        // singIn is just for 'select' of profiles // not yet
        val userForLogin = userRepository.findByEmail(userRequestDto.email)
            ?: throw BadRequestException(ErrorCode.INVALID_SIGN_IN, "WRONG EMAIL ${userRequestDto.email}")
        return if (passwordEncoder.matches(userRequestDto.password, userForLogin.password)) {
            tokenProvider.createToken(userForLogin.id, userForLogin.role)
        } else {
            throw BadRequestException(ErrorCode.INVALID_SIGN_IN, "WRONG PASSWORD")
        }
    }
}

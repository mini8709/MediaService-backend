package com.mediaservice.application

import com.mediaservice.application.dto.SignInRequestDto
import com.mediaservice.application.dto.SignUpRequestDto
import com.mediaservice.application.dto.UserResponseDto
import com.mediaservice.config.JwtTokenProvider
import com.mediaservice.domain.Role
import com.mediaservice.domain.User
import com.mediaservice.domain.repository.UserRepository
import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.ErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository,
    private val tokenProvider: JwtTokenProvider
) {
    @Transactional(readOnly = true)
    fun findById(id: UUID): UserResponseDto {
        return UserResponseDto.from(
            this.userRepository.findById(id) ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST, "NO SUCH USER $id"
            )
        )
    }

    @Transactional
    fun signUp(signUpRequestDto: SignUpRequestDto): UserResponseDto {
        if (this.userRepository.findByEmail(signUpRequestDto.email) != null) {
            throw BadRequestException(ErrorCode.ROW_ALREADY_EXIST, "DUPLICATE_EMAIL")
        }

        return UserResponseDto.from(
            this.userRepository.save(
                User.of(
                    signUpRequestDto.email,
                    signUpRequestDto.password,
                    Role.USER
                )
            )
        )
    }

    @Transactional(readOnly = true)
    fun signIn(signInRequestDto: SignInRequestDto): String {
        // TODO: singIn is just for 'select' of profiles
        val userForLogin = userRepository.findByEmail(signInRequestDto.email)
            ?: throw BadRequestException(ErrorCode.INVALID_SIGN_IN, "WRONG EMAIL ${signInRequestDto.email}")

        return if (signInRequestDto.password == userForLogin.password) {
            tokenProvider.createToken(userForLogin.id!!, userForLogin.role)
        } else {
            throw BadRequestException(ErrorCode.INVALID_SIGN_IN, "WRONG PASSWORD")
        }
    }
}

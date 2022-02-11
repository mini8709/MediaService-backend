package com.mediaservice.application

import com.mediaservice.application.dto.user.PasswordFindRequestDto
import com.mediaservice.application.dto.user.PasswordUpdateRequestDto
import com.mediaservice.application.dto.user.ProfileResponseDto
import com.mediaservice.application.dto.user.SignInRequestDto
import com.mediaservice.application.dto.user.SignInResponseDto
import com.mediaservice.application.dto.user.SignUpRequestDto
import com.mediaservice.application.dto.user.UserResponseDto
import com.mediaservice.application.validator.PasswordFormatValidator
import com.mediaservice.application.validator.PasswordValidator
import com.mediaservice.application.validator.Validator
import com.mediaservice.config.JwtTokenProvider
import com.mediaservice.domain.Role
import com.mediaservice.domain.User
import com.mediaservice.domain.repository.ProfileRepository
import com.mediaservice.domain.repository.RefreshTokenRepository
import com.mediaservice.domain.repository.UserRepository
import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.ErrorCode
import com.mediaservice.infrastructure.GoogleMailSender
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val tokenProvider: JwtTokenProvider,
    private val mailSender: GoogleMailSender
) {
    @Transactional(readOnly = true)
    fun findById(id: UUID): UserResponseDto {
        return UserResponseDto.from(
            this.userRepository.findById(id) ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST, "NO SUCH USER $id"
            )
        )
    }

    @Transactional(readOnly = true)
    fun isDuplicatedByEmail(email: String): Boolean {
        return this.userRepository.findByEmail(email)?.let {
            true
        } ?: false
    }

    @Transactional
    fun signUp(signUpRequestDto: SignUpRequestDto): UserResponseDto {
        this.userRepository.findByEmail(signUpRequestDto.email)?.let {
            throw BadRequestException(ErrorCode.ROW_ALREADY_EXIST, "DUPLICATE EMAIL")
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

    @Transactional
    fun signIn(signInRequestDto: SignInRequestDto): SignInResponseDto {
        val userForLogin = this.userRepository.findByEmail(signInRequestDto.email)
            ?: throw BadRequestException(ErrorCode.INVALID_SIGN_IN, "WRONG EMAIL ${signInRequestDto.email}")

        val validator: Validator = PasswordValidator(signInRequestDto.password, userForLogin.password)

        validator.validate()

        return SignInResponseDto.from(
            this.tokenProvider.createAccessToken(userForLogin.id!!, userForLogin.role),
            this.refreshTokenRepository.save(this.tokenProvider.createRefreshToken()),
            this.profileRepository.findByUserId(userForLogin.id).map { ProfileResponseDto.from(it) }
        )
    }

    @Transactional
    fun updatePassword(id: UUID, passwordUpdateRequestDto: PasswordUpdateRequestDto): UserResponseDto {
        val user = this.userRepository.findById(id)
            ?: throw BadRequestException(ErrorCode.ROW_DOES_NOT_EXIST, "NO SUCH USER $id")

        val validator = PasswordValidator(
            passwordUpdateRequestDto.srcPassword,
            user.password
        )

        validator.linkWith(
            PasswordFormatValidator(passwordUpdateRequestDto.dstPassword)
        )

        validator.validate()
        user.updatePassword(passwordUpdateRequestDto.dstPassword)
        val updateUser = this.userRepository.update(id, user)
            ?: throw BadRequestException(ErrorCode.ROW_DOES_NOT_EXIST, "NO SUCH USER $id")

        return UserResponseDto.from(updateUser)
    }

    @Transactional
    fun findPassword(passwordFindRequestDto: PasswordFindRequestDto): UserResponseDto {
        val user = this.userRepository.findByEmail(passwordFindRequestDto.email)
            ?: throw BadRequestException(ErrorCode.ROW_DOES_NOT_EXIST, "NO SUCH USER WITH EMAIL ${passwordFindRequestDto.email}")

        val newPassword = PasswordGenerator().generate()

        this.mailSender.sendMailWithNewPassword(user.email, newPassword)

        user.updatePassword(newPassword)
        val updateUser = this.userRepository.update(user.id!!, user)
            ?: throw BadRequestException(ErrorCode.ROW_DOES_NOT_EXIST, "NO SUCH USER ${user.id}")

        return UserResponseDto.from(updateUser)
    }

    @Transactional(readOnly = true)
    fun findProfiles(id: UUID): List<ProfileResponseDto> {
        val user = this.userRepository.findById(id)
            ?: throw BadRequestException(ErrorCode.ROW_DOES_NOT_EXIST, "NO SUCH USER $id")

        return this.profileRepository.findByUserId(user.id!!)
            .map { profile -> ProfileResponseDto.from(profile) }
    }
}

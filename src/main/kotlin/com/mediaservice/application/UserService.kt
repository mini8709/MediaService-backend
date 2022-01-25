package com.mediaservice.application

import com.mediaservice.application.dto.user.SignInRequestDto
import com.mediaservice.application.dto.user.SignUpRequestDto
import com.mediaservice.application.dto.user.UserResponseDto
import com.mediaservice.application.validator.PasswordValidator
import com.mediaservice.application.validator.Validator
import com.mediaservice.config.JwtTokenProvider
import com.mediaservice.domain.Role
import com.mediaservice.domain.User
import com.mediaservice.domain.repository.UserRepository
import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.ErrorCode
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository,
    private val tokenProvider: JwtTokenProvider,
    private val mailSender: JavaMailSender
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

    @Transactional(readOnly = true)
    fun signIn(signInRequestDto: SignInRequestDto): String {
        // TODO: singIn is just for 'select' of profiles
        val userForLogin = this.userRepository.findByEmail(signInRequestDto.email)
            ?: throw BadRequestException(ErrorCode.INVALID_SIGN_IN, "WRONG EMAIL ${signInRequestDto.email}")

        val validator: Validator = PasswordValidator(signInRequestDto.password, userForLogin.password)

        validator.validate()
        return tokenProvider.createToken(userForLogin.id!!, userForLogin.role)
    }

    // TODO: Delete at issue #83
    @Transactional(readOnly = true)
    fun testMail() {
        try {
            val message = this.mailSender.createMimeMessage()
            val messageHelper = MimeMessageHelper(message, false, "UTF-8").apply {
                setFrom("cotlin.dev@gmail.com")
                setTo("jeonggon8709@gmail.com")
                setSubject("test Subject")
                setText("test Text")
            }
            this.mailSender.send(message)
        } catch (e: MailException) {
            e.printStackTrace()
        }
    }
}

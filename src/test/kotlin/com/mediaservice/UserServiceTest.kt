package com.mediaservice

import com.mediaservice.application.UserService
import com.mediaservice.application.dto.user.PasswordFindRequestDto
import com.mediaservice.application.dto.user.PasswordUpdateRequestDto
import com.mediaservice.application.dto.user.SignInRequestDto
import com.mediaservice.application.dto.user.SignUpRequestDto
import com.mediaservice.application.dto.user.UserResponseDto
import com.mediaservice.application.infrastructure.GoogleMailSender
import com.mediaservice.config.JwtTokenProvider
import com.mediaservice.domain.Role
import com.mediaservice.domain.User
import com.mediaservice.domain.repository.UserRepository
import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.ErrorCode
import com.mediaservice.exception.ServerUnavailableException
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Properties
import java.util.UUID
import javax.mail.Session
import javax.mail.internet.MimeMessage
import kotlin.test.assertEquals

class UserServiceTest {
    private var userRepository = mockk<UserRepository>()
    private var tokenProvider = mockk<JwtTokenProvider>()
    private val mailSender = mockk<GoogleMailSender>()
    private var userService: UserService = UserService(this.userRepository, this.tokenProvider, this.mailSender)
    private lateinit var user: User
    private lateinit var pwUpdatedUser: User
    private lateinit var id: UUID
    private lateinit var email: String
    private lateinit var prop: Properties
    private lateinit var session: Session
    private lateinit var message: MimeMessage

    @BeforeEach
    fun setup() {
        clearAllMocks()
        this.id = UUID.randomUUID()
        this.email = "test@gmail.com"
        this.user = User(id, email, "1234", Role.USER)
        this.pwUpdatedUser = User(id, email, "test123!!", Role.USER)
        this.prop = Properties()
        this.session = Session.getDefaultInstance(prop)
        this.message = MimeMessage(session)
    }

    @Test
    fun successFindById() {
        // given
        every { userRepository.findById(id) } returns this.user

        // when
        val userResponseDto: UserResponseDto = this.userService.findById(this.id)

        // then
        assertEquals(this.user.email, userResponseDto.email)
    }

    @Test
    fun failFindById() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            every { userRepository.findById(id) } returns null

            // when
            this.userService.findById(this.id)
        }

        // then
        assertEquals(ErrorCode.ROW_DOES_NOT_EXIST, exception.errorCode)
    }

    @Test
    fun successSignUp() {
        // given
        mockkObject(User)
        val signUpRequestDto = SignUpRequestDto("test@gmail.com", "1234")

        every { User.of(signUpRequestDto.email, signUpRequestDto.password, Role.USER) } returns this.user
        every { userRepository.findByEmail(signUpRequestDto.email) } returns null
        every { userRepository.save(user) } returns this.user

        // when
        val userResponseDto: UserResponseDto = this.userService.signUp(signUpRequestDto)

        // then
        assertEquals(this.user.email, userResponseDto.email)
    }

    @Test
    fun failSignUp_DuplicatedEmail() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            val signUpRequestDto = SignUpRequestDto("test@gmail.com", "1234")

            every { userRepository.findByEmail(signUpRequestDto.email) } returns this.user

            // when
            this.userService.signUp(signUpRequestDto)
        }

        // then
        assertEquals(ErrorCode.ROW_ALREADY_EXIST, exception.errorCode)
    }

    @Test
    fun successSignIn() {
        // given
        val signInRequestDto = SignInRequestDto("test@gmail.com", "1234")

        every { userRepository.findByEmail(signInRequestDto.email) } returns this.user
        every { tokenProvider.createToken(user.id!!, user.role) } returns "valid token"

        // when
        val token = this.userService.signIn(signInRequestDto)

        // then
        assertEquals(token, "valid token")
    }

    @Test
    fun failSignIn_CannotFindUser() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            val signInRequestDto = SignInRequestDto("test@gmail.com", "1234")

            every { userRepository.findByEmail(signInRequestDto.email) } returns null

            // when
            this.userService.signIn(signInRequestDto)
        }

        // then
        assertEquals(ErrorCode.INVALID_SIGN_IN, exception.errorCode)
    }

    @Test
    fun failSignIn_IncorrectPassword() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            val signInRequestDto = SignInRequestDto("test@gmail.com", "12345")

            every { userRepository.findByEmail(signInRequestDto.email) } returns this.user

            // when
            this.userService.signIn(signInRequestDto)
        }

        // then
        assertEquals(ErrorCode.INVALID_SIGN_IN, exception.errorCode)
    }

    @Test
    fun successUpdatePassword() {
        // given
        val passwordUpdateRequestDto = PasswordUpdateRequestDto("1234", "test123!!")

        every { userRepository.findById(id) } returns this.user
        every { userRepository.update(id, user) } returns this.pwUpdatedUser

        // when
        val userResponseDto = this.userService.updatePassword(id, passwordUpdateRequestDto)

        // then
        assertEquals(this.pwUpdatedUser.id, userResponseDto.id)
    }

    @Test
    fun failUpdatePassword_CannotFindUser() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            val passwordUpdateRequestDto = PasswordUpdateRequestDto("1234", "test123!!")
            every { userRepository.findById(id) } returns null

            // when
            this.userService.updatePassword(id, passwordUpdateRequestDto)
        }
        // then
        assertEquals(ErrorCode.ROW_DOES_NOT_EXIST, exception.errorCode)
    }

    @Test
    fun failUpdatePassword_InvalidPassword() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            val passwordUpdateRequestDto = PasswordUpdateRequestDto("1233", "test123!!")
            every { userRepository.findById(id) } returns this.user

            // when
            this.userService.updatePassword(id, passwordUpdateRequestDto)
        }
        // then
        assertEquals(ErrorCode.INVALID_SIGN_IN, exception.errorCode)
    }

    @Test
    fun failUpdatePassword_InvalidPasswordFormat() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            val passwordUpdateRequestDto = PasswordUpdateRequestDto("1234", "test1231")
            every { userRepository.findById(id) } returns this.user

            // when
            this.userService.updatePassword(id, passwordUpdateRequestDto)
        }
        // then
        assertEquals(ErrorCode.INVALID_FORMAT, exception.errorCode)
    }

    @Test
    fun failUpdatePassword_CannotFindUpdatedUser() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            val passwordUpdateRequestDto = PasswordUpdateRequestDto("1234", "test123!!")
            every { userRepository.findById(id) } returns this.user
            every { userRepository.update(id, user) } returns null

            // when
            this.userService.updatePassword(id, passwordUpdateRequestDto)
        }
        // then
        assertEquals(ErrorCode.ROW_DOES_NOT_EXIST, exception.errorCode)
    }

    @Test
    fun successFindPassword() {
        // given
        val passwordFindRequestDto = PasswordFindRequestDto(email)
        every { userRepository.findByEmail(email) } returns this.user
        every { userRepository.update(id, user) } returns this.pwUpdatedUser
        every { mailSender.sendMailWithNewPassword(email, any()) } returns Unit

        // when
        val userResponseDto = userService.findPassword(passwordFindRequestDto)

        // then
        assertEquals(this.pwUpdatedUser.id, userResponseDto.id)
    }

    @Test
    fun failFindPassword_CannotFindUser() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            val passwordFindRequestDto = PasswordFindRequestDto(email)
            every { userRepository.findByEmail(email) } returns null

            // when
            this.userService.findPassword(passwordFindRequestDto)
        }
        // then
        assertEquals(ErrorCode.ROW_DOES_NOT_EXIST, exception.errorCode)
    }

    @Test
    fun failFindPassword_CannotFindUpdateUser() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            val passwordFindRequestDto = PasswordFindRequestDto(email)
            every { userRepository.findByEmail(email) } returns user
            every { mailSender.sendMailWithNewPassword(email, any()) } returns Unit
            every { userRepository.update(id, user) } returns null

            // when
            this.userService.findPassword(passwordFindRequestDto)
        }
        // then
        assertEquals(ErrorCode.ROW_DOES_NOT_EXIST, exception.errorCode)
    }

    @Test
    fun failFindPassword_CannotSendMail() {
        val exception = assertThrows(ServerUnavailableException::class.java) {
            // given
            val passwordFindRequestDto = PasswordFindRequestDto(email)
            every { userRepository.findByEmail(email) } returns this.user
            every { userRepository.update(id, user) } returns this.pwUpdatedUser
            every { mailSender.sendMailWithNewPassword(email, any()) } throws ServerUnavailableException(ErrorCode.UNAVAILABLE_MAIL_SERVER, "test")

            // when
            this.userService.findPassword(passwordFindRequestDto)
        }
        // then
        assertEquals(ErrorCode.UNAVAILABLE_MAIL_SERVER, exception.errorCode)
    }
}

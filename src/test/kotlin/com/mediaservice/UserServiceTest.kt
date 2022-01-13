package com.mediaservice

import com.mediaservice.application.UserService
import com.mediaservice.application.dto.user.SignInRequestDto
import com.mediaservice.application.dto.user.SignUpRequestDto
import com.mediaservice.application.dto.user.UserResponseDto
import com.mediaservice.config.JwtTokenProvider
import com.mediaservice.domain.Role
import com.mediaservice.domain.User
import com.mediaservice.domain.repository.UserRepository
import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.ErrorCode
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

class UserServiceTest {
    private var userRepository = mockk<UserRepository>()
    private var tokenProvider = mockk<JwtTokenProvider>()
    private var userService: UserService = UserService(this.userRepository, this.tokenProvider)
    private lateinit var user: User
    private lateinit var id: UUID

    @BeforeEach
    fun setup() {
        clearAllMocks()
        this.id = UUID.randomUUID()
        this.user = User(id, "test@gmail.com", "1234", Role.USER)
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
}

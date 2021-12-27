package com.mediaservice

import com.mediaservice.application.UserService
import com.mediaservice.application.dto.UserRequestDto
import com.mediaservice.application.dto.UserResponseDto
import com.mediaservice.config.JwtTokenProvider
import com.mediaservice.domain.Role
import com.mediaservice.domain.User
import com.mediaservice.domain.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class UserServiceTest(
    @Mock val userRepository: UserRepository,
    @Mock val passwordEncoder: PasswordEncoder,
    @Mock val tokenProvider: JwtTokenProvider
) {
    private val userService: UserService = UserService(this.userRepository, this.passwordEncoder, this.tokenProvider)
    private lateinit var user: User
    private lateinit var id: UUID
    private lateinit var requestDto: UserRequestDto

    @BeforeEach
    fun setUp() {
        this.id = UUID.randomUUID()
        this.user = User(id, "test@gmail.com", "1234", Role.USER)
        this.requestDto = UserRequestDto("test@gmail.com", "1234")
    }

    @Test
    fun successFindById() {
        // given
        given(this.userRepository.findById(this.id)).willReturn(this.user)

        // when
        val userResponseDto: UserResponseDto = this.userService.findById(this.id)

        // then
        assert(this.user.email == userResponseDto.email)
    }

    @Test
    fun successCreateUser() {
        // given
        given(this.passwordEncoder.encode(this.requestDto.password)).willReturn("Encodedpassword")
        given(
            this.userRepository.save(
                this.requestDto.email,
                passwordEncoder.encode(this.requestDto.password)
            )
        ).willReturn(this.user)

        // when
        val userResponseDto: UserResponseDto = this.userService.signUp(this.requestDto)

        // then
        assert(this.user.email == userResponseDto.email)
    }

    @Test
    fun successLogIn() {
        // given
        given(this.userRepository.findByEmail(this.requestDto.email)).willReturn(this.user)
        given(this.passwordEncoder.matches(this.requestDto.password, this.user.password)).willReturn(true)
        given(this.tokenProvider.createToken(this.user.id, Role.USER)).willReturn("valid token")

        // when
        val token = this.userService.signIn(this.requestDto)

        // then
        assert(token == "valid token")
    }
}

package com.mediaservice

import com.mediaservice.application.UserService
import com.mediaservice.application.dto.UserResponseDto
import com.mediaservice.domain.User
import com.mediaservice.domain.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class UserServiceTest(
    @Mock val userRepository: UserRepository
) {
    private val userService: UserService = UserService(this.userRepository)
    private lateinit var user: User
    private lateinit var id: UUID

    @BeforeEach
    fun setUp() {
        this.id = UUID.randomUUID()
        this.user = User(id, "test@gmail.com", "1234")
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
}
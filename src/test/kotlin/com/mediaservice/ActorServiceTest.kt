package com.mediaservice

import com.mediaservice.application.ActorService
import com.mediaservice.application.dto.ActorCreateRequestDto
import com.mediaservice.domain.Actor
import com.mediaservice.domain.Role
import com.mediaservice.domain.User
import com.mediaservice.domain.repository.ActorRepository
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

class ActorServiceTest {
    private val actorRepository = mockk<ActorRepository>()
    private val actorService: ActorService = ActorService(this.actorRepository)
    private lateinit var actorId: UUID
    private lateinit var adminId: UUID
    private lateinit var userId: UUID
    private lateinit var actor: Actor
    private lateinit var admin: User
    private lateinit var user: User

    @BeforeEach
    fun setup() {
        clearAllMocks()
        this.actorId = UUID.randomUUID()
        this.adminId = UUID.randomUUID()
        this.userId = UUID.randomUUID()
        this.actor = Actor(this.actorId, "test Actor")
        this.admin = User(this.adminId, "admin@gmail.com", "1234", Role.ADMIN)
        this.user = User(this.userId, "user@gmail.com", "1234", Role.USER)
    }

    @Test
    fun successCreate() {
        // given
        mockkObject(Actor)
        val actorCreateRequestDto = ActorCreateRequestDto("test Actor")

        every { Actor.of(actorCreateRequestDto.name) } returns this.actor
        every { actorRepository.save(actor) } returns this.actor

        // when
        val actorResponseDto = this.actorService.create(actorCreateRequestDto)

        // then
        assertEquals(this.actor.name, actorResponseDto.name)
    }
}

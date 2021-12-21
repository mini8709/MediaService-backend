package com.mediaservice

import com.mediaservice.application.CreatorService
import com.mediaservice.application.dto.CreatorCreateRequestDto
import com.mediaservice.domain.Creator
import com.mediaservice.domain.Role
import com.mediaservice.domain.User
import com.mediaservice.domain.repository.CreatorRepository
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

class CreatorServiceTest {
    private val creatorRepository = mockk<CreatorRepository>()
    private val creatorService: CreatorService = CreatorService(this.creatorRepository)
    private lateinit var creatorId: UUID
    private lateinit var adminId: UUID
    private lateinit var userId: UUID
    private lateinit var creator: Creator
    private lateinit var admin: User
    private lateinit var user: User

    @BeforeEach
    fun setup() {
        clearAllMocks()
        this.creatorId = UUID.randomUUID()
        this.adminId = UUID.randomUUID()
        this.userId = UUID.randomUUID()
        this.creator = Creator(this.creatorId, "test Creator")
        this.admin = User(this.adminId, "admin@gmail.com", "1234", Role.ADMIN)
        this.user = User(this.userId, "user@gmail.com", "1234", Role.USER)
    }

    @Test
    fun successCreate() {
        // given
        mockkObject(Creator)
        val creatorCreateRequestDto = CreatorCreateRequestDto("test Creator")

        every { Creator.of(creatorCreateRequestDto.name) } returns this.creator
        every { creatorRepository.save(creator) } returns this.creator

        // when
        val creatorResponseDto = this.creatorService.create(creatorCreateRequestDto)

        // then
        assertEquals(this.creator.name, creatorResponseDto.name)
    }
}

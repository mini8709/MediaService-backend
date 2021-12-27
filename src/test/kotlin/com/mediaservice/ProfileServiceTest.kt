package com.mediaservice

import com.mediaservice.application.ProfileService
import com.mediaservice.application.dto.ProfileResponseDto
import com.mediaservice.domain.Profile
import com.mediaservice.domain.Role
import com.mediaservice.domain.User
import com.mediaservice.domain.repository.ProfileRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class ProfileServiceTest(@Mock val profileRepository: ProfileRepository) {
    private val profileService: ProfileService = ProfileService(this.profileRepository)
    private lateinit var profile: Profile
    private lateinit var user: User
    private lateinit var userId: UUID
    private lateinit var profileId: UUID

    @BeforeEach
    fun setUp() {
        this.userId = UUID.randomUUID()
        this.profileId = UUID.randomUUID()
        this.user = User(userId, "test@emai.com", "password", Role.USER)
        this.profile = Profile(profileId, user, "action", "19+", "image_url")
    }

    @Test
    fun successFindById() {
        // given
        given(this.profileRepository.findById(this.profileId)).willReturn(this.profile)

        // when
        val profileResponseDto: ProfileResponseDto = this.profileService.findById(this.profileId)

        // then
        assert(this.profile.name == profileResponseDto.name)
    }
}

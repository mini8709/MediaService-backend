package com.mediaservice

import com.mediaservice.application.ProfileService
import com.mediaservice.application.dto.user.ProfileCreateRequestDto
import com.mediaservice.application.dto.user.SignInProfileResponseDto
import com.mediaservice.domain.Profile
import com.mediaservice.domain.Role
import com.mediaservice.domain.User
import com.mediaservice.domain.repository.ProfileRepository
import com.mediaservice.domain.repository.UserRepository
import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.ErrorCode
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

class ProfileServiceTest {
    private var profileRepository = mockk<ProfileRepository>()
    private var userRepository = mockk<UserRepository>()
    private var profileService: ProfileService = ProfileService(this.profileRepository, this.userRepository)
    private lateinit var userId: UUID
    private lateinit var profileId: UUID
    private lateinit var user: User
    private lateinit var profile: Profile
    private lateinit var profileAlreadyDeleted: Profile
    private lateinit var profileCreateRequestDto: ProfileCreateRequestDto

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        this.userId = UUID.randomUUID()
        this.profileId = UUID.randomUUID()
        this.user = User(userId, "test@emai.com", "password", Role.USER)
        this.profile = Profile(profileId, user, "action", "19+", "image_url", false)
        this.profileAlreadyDeleted = Profile(profileId, user, "action", "19+", "image_url", true)
        this.profileCreateRequestDto = ProfileCreateRequestDto("action", "19+", "image_url")
    }

    @Test
    fun successFindById() {
        // given
        every { profileRepository.findById(profileId) } returns this.profile

        // when
        val profileResponseDto = this.profileService.findById(this.profileId)

        // then
        assertEquals(this.profile.mainImage, profileResponseDto.mainImage)
    }

    @Test
    fun failFindById() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            every { profileRepository.findById(profileId) } returns null

            // when
            this.profileService.findById(this.profileId)
        }

        // then
        assertEquals(ErrorCode.ROW_DOES_NOT_EXIST, exception.errorCode)
    }

    @Test
    fun successFindByUserId() {
        // given
        every { profileRepository.findByUserId(userId) } returns listOf(this.profile)

        // when
        val signInProfileResponseDto: List<SignInProfileResponseDto> = this.profileService.findByUserId(this.userId)

        // then
        assertEquals(this.profile.name, signInProfileResponseDto[0].name)
    }

    @Test
    fun successDeleteProfile() {
        // given
        every {
            profileRepository.findById(profileId)
        } returns profile
        every {
            profileRepository.delete(profileId)
        } returns profile
        // when
        val profileResponseDto = profileService.deleteProfile(profileId)
        // then
        assertEquals(profileResponseDto.profileId, profileId)
    }

    @Test
    fun failDeleteProfile_noProfile() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            every {
                profileRepository.findById(profileId)
            } returns null
            // when
            profileService.deleteProfile(profileId)
        }
        // then
        assertEquals(ErrorCode.ROW_DOES_NOT_EXIST, exception.errorCode)
    }

    @Test
    fun failDeleteProfile_alreadyDeleted() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            every {
                profileRepository.findById(profileId)
            } returns profileAlreadyDeleted
            // when
            profileService.deleteProfile(profileId)
        }
        // then
        assertEquals(ErrorCode.ROW_ALREADY_DELETED, exception.errorCode)
    }

    @Test
    fun successCreateProfile() {
        // given
        every {
            profileRepository.save(any())
        } returns profile
        every {
            profileRepository.countByUserId(userId)
        } returns 0
        every {
            userRepository.findById(userId)
        } returns user

        // when
        val profileResponseDto = profileService.createProfile(profileCreateRequestDto, userId)

        // then
        assertEquals(profileCreateRequestDto.mainImage, profileResponseDto.mainImage)
    }

    @Test
    fun failCreateProfile_noMoreProfiles() {
        // given
        val exception = assertThrows(BadRequestException::class.java) {
            every {
                userRepository.findById(userId)
            } returns user
            every {
                profileRepository.countByUserId(userId)
            } returns 5
            // when
            profileService.createProfile(profileCreateRequestDto, userId)
        }
        // then
        assertEquals(ErrorCode.NO_MORE_ITEM, exception.errorCode)
    }

    @Test
    fun failCreateProfile_noUser() {
        // given
        val exception = assertThrows(BadRequestException::class.java) {
            every {
                userRepository.findById(any())
            } returns null
            // when
            profileService.createProfile(profileCreateRequestDto, userId)
        }
        // then
        assertEquals(ErrorCode.ROW_DOES_NOT_EXIST, exception.errorCode)
    }
}

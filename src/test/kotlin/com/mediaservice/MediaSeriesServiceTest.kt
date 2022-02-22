package com.mediaservice

import com.mediaservice.application.MediaSeriesService
import com.mediaservice.application.dto.media.MediaAllSeriesResponseDto
import com.mediaservice.application.dto.media.MediaDetailRequestDto
import com.mediaservice.domain.Actor
import com.mediaservice.domain.Creator
import com.mediaservice.domain.Genre
import com.mediaservice.domain.Media
import com.mediaservice.domain.MediaAllSeries
import com.mediaservice.domain.MediaSeries
import com.mediaservice.domain.Profile
import com.mediaservice.domain.Role
import com.mediaservice.domain.User
import com.mediaservice.domain.repository.LikeRepository
import com.mediaservice.domain.repository.MediaAllSeriesRepository
import com.mediaservice.domain.repository.MediaRepository
import com.mediaservice.domain.repository.MediaSeriesRepository
import com.mediaservice.domain.repository.ProfileRepository
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

class MediaSeriesServiceTest {
    private var mediaRepository = mockk<MediaRepository>()
    private var mediaSeriesRepository = mockk<MediaSeriesRepository>()
    private var mediaAllSeriesRepository = mockk<MediaAllSeriesRepository>()
    private var profileRepository = mockk<ProfileRepository>()
    private var likeRepository = mockk<LikeRepository>()
    private val mediaSeriesService: MediaSeriesService =
        MediaSeriesService(
            this.mediaRepository, this.mediaSeriesRepository, this.mediaAllSeriesRepository,
            this.profileRepository, this.likeRepository
        )
    private lateinit var media: Media
    private lateinit var mediaSeries: MediaSeries
    private lateinit var mediaAllSeries: MediaAllSeries
    private lateinit var mediaAllSeriesNoActor: MediaAllSeries
    private lateinit var mediaAllSeriesNoGenre: MediaAllSeries
    private lateinit var mediaAllSeriesNoCreator: MediaAllSeries
    private lateinit var user: User
    private lateinit var profile: Profile
    private lateinit var deletedProfile: Profile
    private lateinit var lowRateProfile: Profile
    private lateinit var mediaId: UUID
    private lateinit var mediaSeriesId: UUID
    private lateinit var mediaAllSeriesId: UUID
    private lateinit var userId: UUID
    private lateinit var profileId: UUID
    private lateinit var actorList: List<Actor>
    private lateinit var genreList: List<Genre>
    private lateinit var creatorList: List<Creator>

    @BeforeEach
    fun setup() {
        clearAllMocks()
        this.mediaId = UUID.randomUUID()
        this.mediaSeriesId = UUID.randomUUID()
        this.mediaAllSeriesId = UUID.randomUUID()
        this.userId = UUID.randomUUID()
        this.profileId = UUID.randomUUID()
        this.actorList = listOf(Actor(UUID.randomUUID(), "testActor", false))
        this.genreList = listOf(Genre(UUID.randomUUID(), "testGenre", false))
        this.creatorList = listOf(Creator(UUID.randomUUID(), "testCreator", false))
        this.user = User(userId, "test@gmail.com", "test123!@", Role.USER)
        this.profile = Profile(profileId, this.user, "test profile", "19+", "test.jpg", false)
        this.deletedProfile = Profile(profileId, this.user, "test profile", "19+", "test.jpg", true)
        this.lowRateProfile = Profile(profileId, this.user, "test profile", "15+", "test.jpg", false)
        this.mediaAllSeries = MediaAllSeries(
            mediaAllSeriesId, "test title", "test synopsis", "test trailer",
            "test thumbnail url", "19+", true, false, this.actorList, this.genreList, this.creatorList
        )
        this.mediaAllSeriesNoActor = MediaAllSeries(
            mediaAllSeriesId, "test title", "test synopsis", "test trailer",
            "test thumbnail url", "19+", true, false, null, this.genreList, this.creatorList
        )
        this.mediaAllSeriesNoGenre = MediaAllSeries(
            mediaAllSeriesId, "test title", "test synopsis", "test trailer",
            "test thumbnail url", "19+", true, false, this.actorList, null, this.creatorList
        )
        this.mediaAllSeriesNoCreator = MediaAllSeries(
            mediaAllSeriesId, "test title", "test synopsis", "test trailer",
            "test thumbnail url", "19+", true, false, this.actorList, this.genreList, null
        )
        this.mediaSeries = MediaSeries(
            mediaSeriesId, "season 1", 1, false, this.mediaAllSeries
        )
        this.media = Media(
            mediaId, "test video 1", "test synopsis", 1, "test url",
            "test thumbnail", 100, false, this.mediaSeries
        )
    }

    @Test
    fun successFindMediaSeriesById() {
        // given
        every { mediaSeriesRepository.findById(mediaSeriesId) } returns this.mediaSeries

        // when
        val mediaSeriesResponseDto = this.mediaSeriesService.findMediaSeriesById(this.mediaSeriesId)

        // then
        assertEquals(this.mediaSeries.title, mediaSeriesResponseDto.title)
    }

    @Test
    fun failFindMediaSeriesById() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            every { mediaSeriesRepository.findById(mediaSeriesId) } returns null

            // when
            this.mediaSeriesService.findMediaSeriesById(this.mediaSeriesId)
        }

        // then
        assertEquals(ErrorCode.ROW_DOES_NOT_EXIST, exception.errorCode)
    }

    @Test
    fun successFindMediaAllSeriesById() {
        // given
        every { mediaAllSeriesRepository.findById(mediaAllSeriesId) } returns this.mediaAllSeries

        // when
        val mediaAllSeriesResponseDto: MediaAllSeriesResponseDto =
            this.mediaSeriesService.findMediaAllSeriesById(this.mediaAllSeriesId)

        // then
        assertEquals(this.mediaAllSeries.title, mediaAllSeriesResponseDto.title)
    }

    @Test
    fun failFindById() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            every { mediaAllSeriesRepository.findById(mediaAllSeriesId) } returns null

            // when
            this.mediaSeriesService.findMediaAllSeriesById(this.mediaAllSeriesId)
        }

        // then
        assertEquals(ErrorCode.ROW_DOES_NOT_EXIST, exception.errorCode)
    }

    @Test
    fun successFindDetail() {
        // given
        val mediaDetailRequestDto = MediaDetailRequestDto(mediaAllSeriesId, profileId)

        every {
            profileRepository.findById(mediaDetailRequestDto.profileId)
        } returns this.profile
        every {
            mediaAllSeriesRepository.findById(mediaDetailRequestDto.mediaAllSeriesId)
        } returns this.mediaAllSeries
        every {
            mediaSeriesRepository.findByMediaAllSeriesId(any())
        } returns listOf(this.mediaSeries)
        every {
            mediaRepository.findByMediaSeriesId(any())
        } returns listOf(this.media)
        every {
            likeRepository.isExist(any())
        } returns false

        // when
        val mediaDetailResponseDto = mediaSeriesService.findDetail(mediaDetailRequestDto)

        // then
        assertEquals(mediaDetailRequestDto.mediaAllSeriesId, mediaDetailResponseDto.mediaAllSeriesResponseDto.id)
    }

    @Test
    fun failFindDetail_DeletedProfile() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            val mediaDetailRequestDto = MediaDetailRequestDto(mediaAllSeriesId, profileId)
            every {
                profileRepository.findById(mediaDetailRequestDto.profileId)
            } returns deletedProfile
            every {
                mediaAllSeriesRepository.findById(mediaDetailRequestDto.mediaAllSeriesId)
            } returns this.mediaAllSeries

            // when
            val mediaDetailResponseDto = mediaSeriesService.findDetail(mediaDetailRequestDto)
        }

        // then
        assertEquals(ErrorCode.ROW_ALREADY_DELETED, exception.errorCode)
    }

    @Test
    fun failFindDetail_NoMediaAllSeries() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            val mediaDetailRequestDto = MediaDetailRequestDto(mediaAllSeriesId, profileId)
            every {
                profileRepository.findById(mediaDetailRequestDto.profileId)
            } returns profile
            every {
                mediaAllSeriesRepository.findById(mediaDetailRequestDto.mediaAllSeriesId)
            } returns null

            // when
            val mediaDetailResponseDto = mediaSeriesService.findDetail(mediaDetailRequestDto)
        }

        // then
        assertEquals(ErrorCode.ROW_DOES_NOT_EXIST, exception.errorCode)
    }

    @Test
    fun failFindDetail_RateNotMatch() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            val mediaDetailRequestDto = MediaDetailRequestDto(mediaAllSeriesId, profileId)
            every {
                profileRepository.findById(mediaDetailRequestDto.profileId)
            } returns lowRateProfile
            every {
                mediaAllSeriesRepository.findById(mediaDetailRequestDto.mediaAllSeriesId)
            } returns this.mediaAllSeries

            // when
            val mediaDetailResponseDto = mediaSeriesService.findDetail(mediaDetailRequestDto)
        }
        assertEquals(ErrorCode.RATE_NOT_MATCHED, exception.errorCode)
    }

    @Test
    fun failFindDetail_NoMediaSeriesList() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            val mediaDetailRequestDto = MediaDetailRequestDto(mediaAllSeriesId, profileId)

            every {
                profileRepository.findById(mediaDetailRequestDto.profileId)
            } returns this.profile
            every {
                mediaAllSeriesRepository.findById(mediaDetailRequestDto.mediaAllSeriesId)
            } returns this.mediaAllSeries
            every {
                mediaSeriesRepository.findByMediaAllSeriesId(any())
            } returns null

            // when
            val mediaDetailResponseDto = mediaSeriesService.findDetail(mediaDetailRequestDto)
        }
        assertEquals(ErrorCode.ROW_DOES_NOT_EXIST, exception.errorCode)
    }

    @Test
    fun failFindDetail_NoMediaList() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            val mediaDetailRequestDto = MediaDetailRequestDto(mediaAllSeriesId, profileId)

            every {
                profileRepository.findById(mediaDetailRequestDto.profileId)
            } returns this.profile
            every {
                mediaAllSeriesRepository.findById(mediaDetailRequestDto.mediaAllSeriesId)
            } returns this.mediaAllSeries
            every {
                mediaSeriesRepository.findByMediaAllSeriesId(any())
            } returns listOf(this.mediaSeries)
            every {
                mediaRepository.findByMediaSeriesId(any())
            } returns null

            // when
            val mediaDetailResponseDto = mediaSeriesService.findDetail(mediaDetailRequestDto)
        }
    }
}

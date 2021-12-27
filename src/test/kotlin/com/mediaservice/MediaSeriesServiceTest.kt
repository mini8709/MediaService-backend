package com.mediaservice

import com.mediaservice.application.MediaSeriesService
import com.mediaservice.application.dto.MediaAllSeriesResponseDto
import com.mediaservice.domain.Media
import com.mediaservice.domain.MediaAllSeries
import com.mediaservice.domain.MediaSeries
import com.mediaservice.domain.repository.MediaAllSeriesRepository
import com.mediaservice.domain.repository.MediaSeriesRepository
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
    private var mediaSeriesRepository = mockk<MediaSeriesRepository>()
    private var mediaAllSeriesRepository = mockk<MediaAllSeriesRepository>()
    private val mediaSeriesService: MediaSeriesService =
        MediaSeriesService(this.mediaSeriesRepository, this.mediaAllSeriesRepository)

    private lateinit var media: Media
    private lateinit var mediaSeries: MediaSeries
    private lateinit var mediaAllSeries: MediaAllSeries
    private lateinit var mediaId: UUID
    private lateinit var mediaSeriesId: UUID
    private lateinit var mediaAllSeriesId: UUID

    @BeforeEach
    fun setup() {
        clearAllMocks()
        this.mediaId = UUID.randomUUID()
        this.mediaSeriesId = UUID.randomUUID()
        this.mediaAllSeriesId = UUID.randomUUID()
        this.mediaAllSeries = MediaAllSeries(
            mediaAllSeriesId, "test title", "test synopsis", "test trailer",
            "test thumbnail url", "19+", true
        )
        this.mediaSeries = MediaSeries(mediaSeriesId, "season 1", 1, this.mediaAllSeries)
        this.media = Media(mediaId, "test video 1", "test synopsis", 1, this.mediaSeries)
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
}

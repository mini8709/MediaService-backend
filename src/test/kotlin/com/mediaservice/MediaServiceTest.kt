package com.mediaservice

import com.mediaservice.application.MediaService
import com.mediaservice.application.dto.MediaResponseDto
import com.mediaservice.domain.Media
import com.mediaservice.domain.MediaAllSeries
import com.mediaservice.domain.MediaSeries
import com.mediaservice.domain.repository.MediaRepository
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

class MediaServiceTest {
    private var mediaRepository = mockk<MediaRepository>()
    private val mediaService: MediaService = MediaService(this.mediaRepository)
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
    fun successFindById() {
        // given
        every { mediaRepository.findById(mediaId) } returns this.media

        // when
        val mediaResponseDto: MediaResponseDto = this.mediaService.findById(this.mediaId)

        // then
        assertEquals(this.media.name, mediaResponseDto.name)
    }

    @Test
    fun failFindById() {
        val exception = assertThrows(BadRequestException::class.java) {
            // given
            every { mediaRepository.findById(mediaId) } returns null

            // when
            this.mediaService.findById(this.mediaId)
        }

        // then
        assertEquals(ErrorCode.ROW_DOES_NOT_EXIST, exception.errorCode)
    }
}

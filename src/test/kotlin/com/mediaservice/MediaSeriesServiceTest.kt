package com.mediaservice

import com.mediaservice.application.MediaSeriesService
import com.mediaservice.application.dto.MediaAllSeriesResponseDto
import com.mediaservice.domain.Media
import com.mediaservice.domain.MediaAllSeries
import com.mediaservice.domain.MediaSeries
import com.mediaservice.domain.repository.MediaAllSeriesRepository
import com.mediaservice.domain.repository.MediaSeriesRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class MediaSeriesServiceTest(
    @Mock val mediaSeriesRepository: MediaSeriesRepository,
    @Mock val mediaAllSeriesRepository: MediaAllSeriesRepository
) {
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
        given(this.mediaSeriesRepository.findById(this.mediaSeriesId)).willReturn(this.mediaSeries)

        // when
        val mediaSeriesResponseDto = this.mediaSeriesService.findMediaSeriesById(this.mediaSeriesId)

        // then
        assert(this.mediaSeries.title == mediaSeriesResponseDto.title)
    }

    @Test
    fun successFindMediaAllSeriesById() {
        // given
        given(this.mediaAllSeriesRepository.findById(this.mediaAllSeriesId)).willReturn(this.mediaAllSeries)

        // when
        val mediaAllSeriesResponseDto: MediaAllSeriesResponseDto =
            this.mediaSeriesService.findMediaAllSeriesById(this.mediaAllSeriesId)

        // then
        assert(this.mediaAllSeries.title == mediaAllSeriesResponseDto.title)
    }
}

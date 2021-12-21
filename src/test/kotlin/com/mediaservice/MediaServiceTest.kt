package com.mediaservice

import com.mediaservice.application.MediaService
import com.mediaservice.application.dto.MediaResponseDto
import com.mediaservice.domain.Media
import com.mediaservice.domain.MediaAllSeries
import com.mediaservice.domain.MediaSeries
import com.mediaservice.domain.repository.MediaRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class MediaServiceTest(@Mock val mediaRepository: MediaRepository) {
    private val mediaService: MediaService = MediaService(this.mediaRepository)
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
    fun successFindById() {
        // given
        given(this.mediaRepository.findById(this.mediaId)).willReturn(this.media)

        // when
        val mediaResponseDto: MediaResponseDto = this.mediaService.findById(this.mediaId)

        // then
        assert(this.media.name == mediaResponseDto.name)
    }
}

package com.mediaservice.application

import com.mediaservice.application.dto.media.MediaAllSeriesResponseDto
import com.mediaservice.application.dto.media.MediaSeriesResponseDto
import com.mediaservice.domain.repository.MediaAllSeriesRepository
import com.mediaservice.domain.repository.MediaSeriesRepository
import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.ErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class MediaSeriesService(
    private val mediaSeriesRepository: MediaSeriesRepository,
    private val mediaAllSeriesRepository: MediaAllSeriesRepository
) {
    @Transactional(readOnly = true)
    fun findMediaSeriesById(id: UUID): MediaSeriesResponseDto {
        return MediaSeriesResponseDto.from(
            this.mediaSeriesRepository.findById(id) ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH MEDIA GROUP INFO $id"
            )
        )
    }

    @Transactional(readOnly = true)
    fun findMediaAllSeriesById(id: UUID): MediaAllSeriesResponseDto {
        return MediaAllSeriesResponseDto.from(
            this.mediaAllSeriesRepository.findById(id) ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH MEDIA GROUP $id"
            )
        )
    }
}

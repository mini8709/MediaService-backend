package com.mediaservice.application

import com.mediaservice.application.dto.MediaResponseDto
import com.mediaservice.domain.repository.MediaRepository
import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.ErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class MediaService(private val mediaRepository: MediaRepository) {
    @Transactional(readOnly = true)
    fun findById(id: UUID): MediaResponseDto {
        return MediaResponseDto.from(
            this.mediaRepository.findById(id) ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH MEDIA $id"
            )
        )
    }
}

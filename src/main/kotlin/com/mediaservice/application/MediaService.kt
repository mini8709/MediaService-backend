package com.mediaservice.application

import com.mediaservice.application.dto.media.MediaResponseDto
import com.mediaservice.application.validator.IdEqualValidator
import com.mediaservice.application.validator.IsDeletedValidator
import com.mediaservice.domain.Profile
import com.mediaservice.domain.repository.MediaRepository
import com.mediaservice.domain.repository.ProfileRepository
import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.ErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class MediaService(
    private val profileRepository: ProfileRepository,
    private val mediaRepository: MediaRepository
) {
    @Transactional(readOnly = true)
    fun findById(
        userId: UUID,
        profileId: UUID,
        id: UUID
    ): MediaResponseDto {
        val profile = this.profileRepository.findById(profileId)
            ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH PROFILE $profileId"
            )

        val validator = IsDeletedValidator(profile.isDeleted, Profile.DOMAIN)
        validator.linkWith(IdEqualValidator(userId, profile.user.id!!))
        validator.validate()

        return MediaResponseDto.from(
            this.mediaRepository.findById(id) ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH MEDIA $id"
            )
        )
    }

    @Transactional(readOnly = true)
    fun findByMediaSeries(
        userId: UUID,
        profileId: UUID,
        id: UUID
    ): List<MediaResponseDto> {
        val profile = this.profileRepository.findById(profileId)
            ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH PROFILE $profileId"
            )

        val validator = IsDeletedValidator(profile.isDeleted, Profile.DOMAIN)
        validator.linkWith(IdEqualValidator(userId, profile.user.id!!))
        validator.validate()

        return this.mediaRepository.findByMediaSeriesId(id)?.map {
            MediaResponseDto.from(it)
        } ?: throw BadRequestException(
            ErrorCode.ROW_DOES_NOT_EXIST,
            "NO SUCH MEDIA LIST WITH MEDIA SERIES $id"
        )
    }

    @Transactional
    fun deleteById(
        id: UUID
    ): MediaResponseDto {
        return MediaResponseDto.from(
            this.mediaRepository.deleteById(id) ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH MEDIA $id"
            )
        )
    }
}

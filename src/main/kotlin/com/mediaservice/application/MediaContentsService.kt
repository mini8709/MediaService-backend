package com.mediaservice.application

import com.mediaservice.application.dto.media.MediaContentsCreateRequestDto
import com.mediaservice.application.dto.media.MediaContentsResponseDto
import com.mediaservice.application.dto.media.MediaSeriesCreateRequestDto
import com.mediaservice.application.dto.media.MediaSeriesResponseDto
import com.mediaservice.application.validator.IdEqualValidator
import com.mediaservice.application.validator.IsDeletedValidator
import com.mediaservice.application.validator.RateMatchValidator
import com.mediaservice.domain.Like
import com.mediaservice.domain.MediaContents
import com.mediaservice.domain.MediaSeries
import com.mediaservice.domain.Profile
import com.mediaservice.domain.repository.LikeRepository
import com.mediaservice.domain.repository.MediaContentsRepository
import com.mediaservice.domain.repository.MediaRepository
import com.mediaservice.domain.repository.MediaSeriesRepository
import com.mediaservice.domain.repository.ProfileRepository
import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.ErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class MediaContentsService(
    private val mediaRepository: MediaRepository,
    private val mediaSeriesRepository: MediaSeriesRepository,
    private val mediaContentsRepository: MediaContentsRepository,
    private val profileRepository: ProfileRepository,
    private val likeRepository: LikeRepository
) {
    @Transactional(readOnly = true)
    fun findMediaSeriesById(
        userId: UUID,
        profileId: UUID,
        id: UUID
    ): MediaSeriesResponseDto {
        val profile = this.profileRepository.findById(profileId)
            ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH PROFILE $profileId"
            )

        val validator = IsDeletedValidator(profile.isDeleted, Profile.DOMAIN)
        validator.linkWith(IdEqualValidator(userId, profile.user.id!!))
        validator.validate()

        return MediaSeriesResponseDto.from(
            this.mediaSeriesRepository.findById(id) ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH MEDIA SERIES $id"
            )
        )
    }

    @Transactional
    fun createMediaSeries(
        id: UUID,
        mediaSeriesCreateRequestDto: MediaSeriesCreateRequestDto
    ): MediaSeriesResponseDto {
        val mediaContents = this.mediaContentsRepository.findById(id)
            ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH MEDIA CONTENTS $id"
            )

        return MediaSeriesResponseDto.from(
            this.mediaSeriesRepository.save(
                MediaSeries.of(
                    mediaSeriesCreateRequestDto.title,
                    mediaSeriesCreateRequestDto.order,
                    mediaContents
                )
            )
        )
    }

    @Transactional
    fun deleteMediaSeriesById(id: UUID): MediaSeriesResponseDto {
        return MediaSeriesResponseDto.from(
            this.mediaSeriesRepository.deleteById(id) ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH MEDIA SERIES $id"
            )
        )
    }

    @Transactional(readOnly = true)
    fun findMediaContentsById(
        userId: UUID,
        profileId: UUID,
        mediaContentsId: UUID
    ): MediaContentsResponseDto {
        val profile = this.profileRepository.findById(profileId)
            ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH PROFILE $profileId"
            )

        val validator = IsDeletedValidator(profile.isDeleted, Profile.DOMAIN)

        val mediaContents = this.mediaContentsRepository.findById(mediaContentsId)
            ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH MEDIA CONTENTS $mediaContentsId"
            )

        validator
            .linkWith(RateMatchValidator(profile.rate, mediaContents.rate))
            .linkWith(IdEqualValidator(userId, profile.user.id!!))
        validator.validate()

        val mediaSeriesList = this.mediaSeriesRepository.findByMediaAllSeriesId(mediaContents.id!!)
            ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH MEDIA SERIES LIST WITH MEDIA CONTENTS ${mediaContents.id}"
            )

        val mediaList = this.mediaRepository.findByMediaSeriesId(mediaSeriesList[0].id!!)
            ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH MEDIA LIST WITH MEDIA SERIES ${mediaSeriesList[0].id}"
            )

        val isLike = this.likeRepository.isExist(Like.of(profile, mediaContents))

        return MediaContentsResponseDto.from(mediaContents, mediaSeriesList, mediaList, isLike)
    }

    @Transactional
    fun createMediaContents(
        mediaContentsCreateRequestDto: MediaContentsCreateRequestDto
    ): MediaContentsResponseDto {
        val mediaContents = this.mediaContentsRepository.save(
            MediaContents.of(
                mediaContentsCreateRequestDto.title,
                mediaContentsCreateRequestDto.synopsis,
                mediaContentsCreateRequestDto.trailer,
                mediaContentsCreateRequestDto.thumbnail,
                mediaContentsCreateRequestDto.rate,
                mediaContentsCreateRequestDto.isSeries
            )
        )

        val mediaSeries = this.mediaSeriesRepository.save(
            MediaSeries.of(
                mediaContentsCreateRequestDto.mediaSeriesTitle,
                1,
                mediaContents
            )
        )

        return MediaContentsResponseDto.from(
            mediaContents,
            listOf(mediaSeries),
            listOf(),
            false
        )
    }

    @Transactional
    fun deleteMediaContentsById(id: UUID): MediaContentsResponseDto {
        return MediaContentsResponseDto.from(
            this.mediaContentsRepository.deleteById(id) ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH MEDIA CONTENTS $id"
            ),
            listOf(),
            listOf(),
            false
        )
    }
}

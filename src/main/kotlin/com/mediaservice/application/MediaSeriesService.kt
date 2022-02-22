package com.mediaservice.application

import com.mediaservice.application.dto.media.MediaAllSeriesResponseDto
import com.mediaservice.application.dto.media.MediaDetailRequestDto
import com.mediaservice.application.dto.media.MediaDetailResponseDto
import com.mediaservice.application.dto.media.MediaSeriesResponseDto
import com.mediaservice.application.validator.IsDeletedValidator
import com.mediaservice.application.validator.RateMatchValidator
import com.mediaservice.domain.Like
import com.mediaservice.domain.Profile
import com.mediaservice.domain.repository.LikeRepository
import com.mediaservice.domain.repository.MediaAllSeriesRepository
import com.mediaservice.domain.repository.MediaRepository
import com.mediaservice.domain.repository.MediaSeriesRepository
import com.mediaservice.domain.repository.ProfileRepository
import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.ErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class MediaSeriesService(
    private val mediaRepository: MediaRepository,
    private val mediaSeriesRepository: MediaSeriesRepository,
    private val mediaAllSeriesRepository: MediaAllSeriesRepository,
    private val profileRepository: ProfileRepository,
    private val likeRepository: LikeRepository
) {
    @Transactional(readOnly = true)
    fun findMediaSeriesById(id: UUID): MediaSeriesResponseDto {
        return MediaSeriesResponseDto.from(
            this.mediaSeriesRepository.findById(id) ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH MEDIA SERIES $id"
            )
        )
    }

    @Transactional(readOnly = true)
    fun findMediaAllSeriesById(id: UUID): MediaAllSeriesResponseDto {
        return MediaAllSeriesResponseDto.from(
            this.mediaAllSeriesRepository.findById(id) ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH MEDIA ALL SERIES $id"
            )
        )
    }

    @Transactional(readOnly = true)
    fun findDetail(mediaDetailRequestDto: MediaDetailRequestDto): MediaDetailResponseDto {
        val profile = this.profileRepository.findById(mediaDetailRequestDto.profileId)
            ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH PROFILE ${mediaDetailRequestDto.profileId}"
            )

        val validator = IsDeletedValidator(profile.isDeleted, Profile.DOMAIN)

        val mediaAllSeries = this.mediaAllSeriesRepository.findById(mediaDetailRequestDto.mediaAllSeriesId)
            ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH MEDIA ALL SERIES ${mediaDetailRequestDto.mediaAllSeriesId}"
            )

        val rateMatchValidator = RateMatchValidator(profile.rate, mediaAllSeries.rate)

        validator.linkWith(rateMatchValidator)
        validator.validate()

        val mediaSeriesList = this.mediaSeriesRepository.findByMediaAllSeriesId(mediaAllSeries.id)
            ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH MEDIA SERIES LIST WITH MEDIA ALL SERIES ${mediaAllSeries.id}"
            )

        val mediaList = this.mediaRepository.findByMediaSeriesId(mediaSeriesList[0].id)
            ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH MEDIA LIST WITH MEDIA SERIES ${mediaSeriesList[0].id}"
            )

        val isLike = this.likeRepository.isExist(Like.of(profile, mediaAllSeries))

        return MediaDetailResponseDto.from(mediaAllSeries, mediaSeriesList, mediaList, isLike)
    }
}

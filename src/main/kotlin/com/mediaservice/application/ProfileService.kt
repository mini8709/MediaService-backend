package com.mediaservice.application

import com.mediaservice.application.dto.user.LikeRequestDto
import com.mediaservice.application.dto.user.LikeResponseDto
import com.mediaservice.application.dto.user.ProfileCreateRequestDto
import com.mediaservice.application.dto.user.ProfileResponseDto
import com.mediaservice.application.dto.user.ProfileUpdateRequestDto
import com.mediaservice.application.dto.user.SignInProfileResponseDto
import com.mediaservice.application.validator.IsDeletedValidator
import com.mediaservice.application.validator.ProfileNumberValidator
import com.mediaservice.application.validator.UserEqualValidator
import com.mediaservice.application.validator.Validator
import com.mediaservice.domain.Like
import com.mediaservice.domain.Profile
import com.mediaservice.domain.repository.LikeRepository
import com.mediaservice.domain.repository.MediaAllSeriesRepository
import com.mediaservice.domain.repository.ProfileRepository
import com.mediaservice.domain.repository.UserRepository
import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.ErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ProfileService(
    private val profileRepository: ProfileRepository,
    private val userRepository: UserRepository,
    private val mediaAllSeriesRepository: MediaAllSeriesRepository,
    private val likeRepository: LikeRepository
) {
    @Transactional(readOnly = true)
    fun findById(id: UUID): ProfileResponseDto {
        return ProfileResponseDto.from(
            this.profileRepository.findById(id)
                ?: throw BadRequestException(ErrorCode.ROW_DOES_NOT_EXIST, "NO SUCH PROFILE $id")
        )
    }

    @Transactional(readOnly = true)
    fun findByUserId(id: UUID): List<SignInProfileResponseDto> {
        return this.profileRepository.findByUserId(id)
            .map { profile -> SignInProfileResponseDto.from(profile) }
    }

    @Transactional
    fun create(
        profileCreateRequestDto: ProfileCreateRequestDto,
        userId: UUID
    ): ProfileResponseDto {
        val user = userRepository.findById(userId)
            ?: throw BadRequestException(ErrorCode.ROW_DOES_NOT_EXIST, "NO SUCH USER $userId")

        val numOfProfiles = profileRepository.countByUserId(userId)

        val validator: Validator = ProfileNumberValidator(numOfProfiles.toInt(), userId)
        validator.validate()

        return ProfileResponseDto.from(
            this.profileRepository.save(
                Profile.of(
                    name = profileCreateRequestDto.name,
                    rate = profileCreateRequestDto.rate,
                    mainImage = profileCreateRequestDto.mainImage,
                    user = user
                )
            )
        )
    }

    @Transactional
    fun delete(
        userId: UUID,
        id: UUID
    ): ProfileResponseDto {
        val user = userRepository.findById(userId)
            ?: throw BadRequestException(ErrorCode.ROW_DOES_NOT_EXIST, "NO SUCH USER $userId")

        val profileForDelete = this.profileRepository.findById(id) ?: throw BadRequestException(
            ErrorCode.ROW_DOES_NOT_EXIST, "NO SUCH PROFILE $id"
        )

        val validator = IsDeletedValidator(profileForDelete.isDeleted, Profile.DOMAIN)
        validator.linkWith(UserEqualValidator(user.id!!, profileForDelete.user.id!!))
        validator.validate()

        return ProfileResponseDto.from(
            this.profileRepository.delete(
                id
            ) ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST, "NO SUCH PROFILE $id"
            )
        )
    }

    @Transactional
    fun update(
        userId: UUID,
        profileId: UUID,
        profileUpdateRequestDto: ProfileUpdateRequestDto
    ): ProfileResponseDto? {
        val user = userRepository.findById(userId)
            ?: throw BadRequestException(ErrorCode.ROW_DOES_NOT_EXIST, "NO SUCH USER $userId")

        val profileForUpdate = this.profileRepository.findById(profileId) ?: throw BadRequestException(
            ErrorCode.ROW_DOES_NOT_EXIST, "NO SUCH PROFILE $profileId"
        )

        val validator: Validator = IsDeletedValidator(profileForUpdate.isDeleted, Profile.DOMAIN)
        validator.linkWith(UserEqualValidator(user.id!!, profileForUpdate.user.id!!))
        validator.validate()

        profileForUpdate.update(profileUpdateRequestDto.name, profileUpdateRequestDto.mainImage, profileUpdateRequestDto.rate)

        return ProfileResponseDto.from(
            this.profileRepository.update(
                profileForUpdate
            ) ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST, "NO SUCH PROFILE $profileId"
            )
        )
    }

    @Transactional
    fun createLike(
        likeRequestDto: LikeRequestDto
    ): LikeResponseDto {
        val profile = this.profileRepository.findById(likeRequestDto.profileId)
            ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH PROFILE ${likeRequestDto.profileId}"
            )

        val mediaAllSeries = this.mediaAllSeriesRepository.findById(likeRequestDto.mediaAllSeriesId)
            ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH MEDIA ALL SERIES ${likeRequestDto.mediaAllSeriesId}"
            )

        val validator: Validator = IsDeletedValidator(profile.isDeleted, Profile.DOMAIN)
        validator.validate()

        return LikeResponseDto.from(
            this.likeRepository.save(Like.of(profile, mediaAllSeries))
        )
    }

    @Transactional
    fun deleteLike(
        likeRequestDto: LikeRequestDto
    ): LikeResponseDto {
        val profile = this.profileRepository.findById(likeRequestDto.profileId)
            ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH PROFILE ${likeRequestDto.profileId}"
            )

        val mediaAllSeries = this.mediaAllSeriesRepository.findById(likeRequestDto.mediaAllSeriesId)
            ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH MEDIA ALL SERIES ${likeRequestDto.mediaAllSeriesId}"
            )

        val validator: Validator = IsDeletedValidator(profile.isDeleted, Profile.DOMAIN)
        validator.validate()

        return LikeResponseDto.from(
            this.likeRepository.delete(Like.of(profile, mediaAllSeries))
        )
    }
}

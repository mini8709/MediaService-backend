package com.mediaservice.application

import com.mediaservice.application.dto.media.WishContentRequestDto
import com.mediaservice.application.dto.media.WishContentResponseDto
import com.mediaservice.application.validator.IdEqualValidator
import com.mediaservice.application.validator.IsDeletedValidator
import com.mediaservice.domain.Profile
import com.mediaservice.domain.WishContent
import com.mediaservice.domain.repository.MediaContentsRepository
import com.mediaservice.domain.repository.ProfileRepository
import com.mediaservice.domain.repository.WishContentRepository
import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.ErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class WishContentService(
    private val wishContentRepository: WishContentRepository,
    private val profileRepository: ProfileRepository,
    private val mediaContentsRepository: MediaContentsRepository
) {

    @Transactional(readOnly = true)
    fun findByProfileId(
        userId: UUID,
        profileId: UUID
    ): List<WishContentResponseDto> {
        val profile = this.profileRepository.findById(profileId)
            ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH PROFILE $profileId"
            )

        val validator = IsDeletedValidator(profile.isDeleted, Profile.DOMAIN)
        validator.linkWith(IdEqualValidator(userId, profile.user.id!!))
        validator.validate()

        return this.wishContentRepository.findByProfileId(profileId)
            .map { wishContent -> WishContentResponseDto.from(wishContent) }
    }

    @Transactional
    fun createWishContent(
        userId: UUID,
        profileId: UUID,
        wishContentRequestDto: WishContentRequestDto
    ): WishContentResponseDto {
        val mediaContents = mediaContentsRepository.findById(wishContentRequestDto.mediaContentsId)
            ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH USER ${wishContentRequestDto.mediaContentsId}"
            )

        val profile = profileRepository.findById(profileId)
            ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH USER $profileId"
            )

        if (wishContentRepository.existsByProfileIdAndMediaAllSeriesId(
                profileId,
                wishContentRequestDto.mediaContentsId
            )
        )
            throw BadRequestException(
                ErrorCode.ROW_ALREADY_EXIST,
                "${wishContentRequestDto.mediaContentsId} is Already Inserted in $profileId"
            )

        val validator = IsDeletedValidator(profile.isDeleted, Profile.DOMAIN)
        validator.linkWith(IdEqualValidator(userId, profile.user.id!!))
        validator.validate()

        return WishContentResponseDto.from(
            this.wishContentRepository.save(
                WishContent.of(
                    profile = profile,
                    mediaContents = mediaContents
                )
            )
        )
    }

    @Transactional
    fun deleteWishContent(
        userId: UUID,
        profileId: UUID,
        wishContentRequestDto: WishContentRequestDto
    ): WishContentResponseDto {
        val mediaContents = mediaContentsRepository.findById(wishContentRequestDto.mediaContentsId)
            ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH USER ${wishContentRequestDto.mediaContentsId}"
            )

        val profile = profileRepository.findById(profileId)
            ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST,
                "NO SUCH USER $profileId"
            )

        val validator = IsDeletedValidator(profile.isDeleted, Profile.DOMAIN)
        validator.linkWith(IdEqualValidator(userId, profile.user.id!!))
        validator.validate()

        return WishContentResponseDto.from(
            this.wishContentRepository.delete(
                WishContent.of(
                    profile = profile,
                    mediaContents = mediaContents
                )
            )
        )
    }
}

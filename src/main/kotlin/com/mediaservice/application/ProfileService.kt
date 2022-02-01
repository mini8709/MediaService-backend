package com.mediaservice.application

import com.mediaservice.application.dto.user.ProfileResponseDto
import com.mediaservice.application.dto.user.SignInProfileResponseDto
import com.mediaservice.application.validator.IsDeletedValidator
import com.mediaservice.domain.Profile
import com.mediaservice.domain.repository.ProfileRepository
import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.ErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ProfileService(private val profileRepository: ProfileRepository) {
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
    fun deleteProfile(id: UUID): ProfileResponseDto {
        val profileForUpdate = this.profileRepository.findById(id) ?: throw BadRequestException(
            ErrorCode.ROW_DOES_NOT_EXIST, "NO SUCH PROFILE $id"
        )

        val validator = IsDeletedValidator(profileForUpdate.isDeleted, Profile.DOMAIN)
        validator.validate()

        return ProfileResponseDto.from(
            this.profileRepository.delete(
                id
            ) ?: throw BadRequestException(
                ErrorCode.ROW_DOES_NOT_EXIST, "NO SUCH PROFILE $id"
            )
        )
    }
}

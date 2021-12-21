package com.mediaservice.domain.repository

import com.mediaservice.domain.Profile
import com.mediaservice.domain.ProfileEntity
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ProfileRepository {
    fun findById(id: UUID): Profile? {
        return ProfileEntity.findById(id)?.let { Profile.from(it) }
    }
}

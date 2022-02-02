package com.mediaservice.domain.repository

import com.mediaservice.domain.Profile
import com.mediaservice.domain.ProfileEntity
import com.mediaservice.domain.ProfileTable
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ProfileRepository {
    fun findById(id: UUID): Profile? {
        return ProfileEntity.findById(id)?.let { Profile.from(it) }
    }

    fun findByUserId(id: UUID): List<Profile> {
        return ProfileEntity.find {
            ProfileTable.user_id eq id
            ProfileTable.isDeleted eq false
        }.map { Profile.from(it) }
    }

    fun countByUserId(id: UUID): Long {
        return ProfileEntity.find {
            ProfileTable.user_id eq id
            ProfileTable.isDeleted eq false
        }.count()
    }

    fun save(profile: Profile): Profile {
        return ProfileEntity.new {
            name = profile.name
            mainImage = profile.mainImage
            rate = profile.rate
        }.let {
            Profile.from(it)
        }
    }

    fun delete(id: UUID): Profile? {
        return ProfileEntity.findById(id)?.let {
            it.isDeleted = true
            return Profile.from(it)
        }
    }

    fun update(profile: Profile): Profile? {
        return profile.id?.let { uuid ->
            ProfileEntity.findById(uuid)?.let { profileEntity ->
                profileEntity.name = profile.name
                profileEntity.mainImage = profile.mainImage
                profileEntity.rate = profile.rate
                return Profile.from(profileEntity)
            }
        }
    }
}

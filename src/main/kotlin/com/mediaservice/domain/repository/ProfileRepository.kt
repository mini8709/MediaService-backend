package com.mediaservice.domain.repository

import com.mediaservice.domain.Profile
import com.mediaservice.domain.ProfileEntity
import com.mediaservice.domain.ProfileTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ProfileRepository {
    fun findById(id: UUID): Profile? {
        return ProfileEntity.findById(id)?.let { Profile.from(it) }
    }

    fun findByUserId(id: UUID): List<Profile> {
        return ProfileEntity.find {
            ProfileTable.user_id eq id and (ProfileTable.isDeleted eq false)
        }.map { Profile.from(it) }
    }

    fun countByUserId(id: UUID): Long {
        return ProfileEntity.find {
            ProfileTable.user_id eq id and (ProfileTable.isDeleted eq false)
        }.count()
    }

    fun save(profile: Profile): Profile {
        val id = ProfileTable.insert {
            it[name] = profile.name
            it[mainImage] = profile.mainImage
            it[rate] = profile.rate
            it[isDeleted] = false
            it[user_id] = profile.user.id
        } get ProfileTable.id
        profile.id = id.value
        return profile
    }

    fun delete(id: UUID): Profile? {
        return ProfileEntity.findById(id)?.let {
            it.isDeleted = true
            return Profile.from(it)
        }
    }

    fun update(profile: Profile): Profile? {
        return ProfileEntity.findById(profile.id!!)?.let { profileEntity ->
            profileEntity.name = profile.name
            profileEntity.mainImage = profile.mainImage
            profileEntity.rate = profile.rate
            return Profile.from(profileEntity)
        }
    }
}

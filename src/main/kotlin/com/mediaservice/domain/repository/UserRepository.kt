package com.mediaservice.domain.repository

import com.mediaservice.domain.User
import com.mediaservice.domain.UserEntity
import com.mediaservice.domain.UserTable
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class UserRepository {
    fun findById(id: UUID): User? {
        return UserEntity.findById(id)?.let { User.from(it) }
    }

    fun save(emailInput: String, passwordInput: String): User {
        return UserEntity.new {
            email = emailInput
            password = passwordInput
        }.let { User.from(it) }
    }

    fun findByEmail(username: String): User? {
        return UserEntity.find { UserTable.email eq username }.firstOrNull()?.let { User.from(it) }
    }
}

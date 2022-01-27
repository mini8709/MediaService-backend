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

    fun save(user: User): User {
        return UserEntity.new {
            email = user.email
            password = user.password
            role = user.role
        }.let { User.from(it) }
    }

    fun findByEmail(username: String): User? {
        return UserEntity.find { UserTable.email eq username }.firstOrNull()?.let { User.from(it) }
    }

    fun update(id: UUID, user: User): User? {
        return UserEntity.findById(id)?.let {
            it.email = user.email
            it.password = user.password
            it.role = user.role
            return User.from(it)
        }
    }
}

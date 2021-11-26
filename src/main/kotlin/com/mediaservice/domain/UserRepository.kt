package com.mediaservice.domain

import org.jetbrains.exposed.sql.select
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserRepository {
    fun findById(id: UUID): User {
        return User.from(UserTable.select { UserTable.id eq id }.limit(1).first())
    }
}
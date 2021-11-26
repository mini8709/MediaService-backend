package com.mediaservice.domain

import com.mediaservice.application.dto.UserResponseDto
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import java.util.*

object UserTable: UUIDTable("TB_USER") {
    val email: Column<String> = varchar("email", 255)
    val password: Column<String> = varchar("password", 255)
}

class User(var id: UUID, var email: String, var password: String) {
    companion object {
        fun from(resultRow: ResultRow) = User(
            id = resultRow[UserTable.id].value,
            email = resultRow[UserTable.email],
            password = resultRow[UserTable.password]
        )
    }
}
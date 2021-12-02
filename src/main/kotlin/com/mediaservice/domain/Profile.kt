package com.mediaservice.domain

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.*

object ProfileTable: UUIDTable("TB_PROFILE"){
    val user = reference("user", UserTable)
    val name: Column<String> = varchar("name", 255)
    val rate: Column<String> = varchar("age_rate", 255)
    val mainImage: Column<String> = varchar("main_image", 255)
}

class Profile(var id: UUID, var user: User, var name: String, var rate: String, var mainImage: String){
    companion object {
        fun from(profileEntity: ProfileEntity) = Profile(
                id = profileEntity.id.value,
                user = User.from(profileEntity.user),
                name = profileEntity.name,
                rate = profileEntity.rate,
                mainImage = profileEntity.mainImage
        )
    }
}

class ProfileEntity(id: EntityID<UUID>): UUIDEntity(id){
    companion object: UUIDEntityClass<ProfileEntity>(ProfileTable)
    var user by UserEntity referencedOn ProfileTable.user
    var name by ProfileTable.name
    var rate by ProfileTable.rate
    var mainImage by ProfileTable.mainImage

}
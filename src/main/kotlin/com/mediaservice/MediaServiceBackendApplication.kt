package com.mediaservice

import com.mediaservice.domain.ActorTable
import com.mediaservice.domain.CreatorTable
import com.mediaservice.domain.GenreTable
import com.mediaservice.domain.MediaAllSeriesTable
import com.mediaservice.domain.MediaSeriesTable
import com.mediaservice.domain.MediaTable
import com.mediaservice.domain.ProfileTable
import com.mediaservice.domain.UserTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@Retention(AnnotationRetention.BINARY)
annotation class NoCoverageGenerated

typealias NoCoverage = NoCoverageGenerated

@NoCoverage
@SpringBootApplication
class MediaServiceBackendApplication

@NoCoverage
fun main(args: Array<String>) {
    runApplication<MediaServiceBackendApplication>(*args)

    if (System.getProperty("spring.profiles.active") == "local") {
        transaction {
            SchemaUtils.drop(
                UserTable, ProfileTable, MediaTable, MediaSeriesTable, MediaAllSeriesTable, ActorTable, CreatorTable, GenreTable
            )
            SchemaUtils.create(
                UserTable, ProfileTable, MediaTable, MediaSeriesTable, MediaAllSeriesTable, ActorTable, CreatorTable, GenreTable
            )
        }
    }
}

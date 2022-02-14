package com.mediaservice

import com.mediaservice.infrastructure.AppInitiator
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
        AppInitiator.localInit()
    }
}

package com.mediaservice

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
}

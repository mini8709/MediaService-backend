package com.mediaservice.web

import com.mediaservice.application.MediaService
import com.mediaservice.application.dto.media.MediaResponseDto
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/media")
class MediaController(private val mediaService: MediaService) {
    @GetMapping("/{id}")
    fun findById(
        @AuthenticationPrincipal userId: String,
        @RequestHeader(value = "profileId") profileId: String,
        @PathVariable id: UUID
    ): MediaResponseDto {
        return this.mediaService.findById(
            UUID.fromString(userId),
            UUID.fromString(profileId),
            id
        )
    }

    @GetMapping("/series/{id}")
    fun findByMediaSeries(
        @AuthenticationPrincipal userId: String,
        @RequestHeader(value = "profileId") profileId: String,
        @PathVariable id: UUID
    ): List<MediaResponseDto> {
        return this.mediaService.findByMediaSeries(
            UUID.fromString(userId),
            UUID.fromString(profileId),
            id
        )
    }

    @DeleteMapping("/{id}")
    fun deleteById(
        @PathVariable id: UUID
    ): MediaResponseDto {
        return this.mediaService.deleteById(
            id
        )
    }
}

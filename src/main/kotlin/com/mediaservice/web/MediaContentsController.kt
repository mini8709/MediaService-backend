package com.mediaservice.web

import com.mediaservice.application.MediaContentsService
import com.mediaservice.application.dto.media.MediaContentsResponseDto
import com.mediaservice.application.dto.media.MediaSeriesResponseDto
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/media-contents")
class MediaContentsController(private val mediaSeriesService: MediaContentsService) {
    @GetMapping("/series/{id}")
    fun findMediaSeriesById(
        @AuthenticationPrincipal userId: String,
        @RequestHeader(value = "profileId") profileId: String,
        @PathVariable id: UUID
    ): MediaSeriesResponseDto {
        return this.mediaSeriesService.findMediaSeriesById(
            UUID.fromString(userId),
            UUID.fromString(profileId),
            id
        )
    }

    @GetMapping("/{id}")
    fun findMediaContentsById(
        @AuthenticationPrincipal userId: String,
        @RequestHeader(value = "profileId") profileId: String,
        @PathVariable id: UUID
    ): MediaContentsResponseDto {
        return this.mediaSeriesService.findMediaContentsById(
            UUID.fromString(userId),
            UUID.fromString(profileId),
            id
        )
    }
}

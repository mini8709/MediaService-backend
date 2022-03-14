package com.mediaservice.web

import com.mediaservice.application.MediaContentsService
import com.mediaservice.application.dto.media.MediaContentsCreateRequestDto
import com.mediaservice.application.dto.media.MediaContentsResponseDto
import com.mediaservice.application.dto.media.MediaSeriesCreateRequestDto
import com.mediaservice.application.dto.media.MediaSeriesResponseDto
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/media-contents")
class MediaContentsController(private val mediaContentsService: MediaContentsService) {
    @GetMapping("/series/{id}")
    fun findMediaSeriesById(
        @AuthenticationPrincipal userId: String,
        @RequestHeader(value = "profileId") profileId: String,
        @PathVariable id: UUID
    ): MediaSeriesResponseDto {
        return this.mediaContentsService.findMediaSeriesById(
            UUID.fromString(userId),
            UUID.fromString(profileId),
            id
        )
    }

    @PostMapping("/series/{id}")
    fun createMediaSeries(
        @PathVariable id: UUID,
        @RequestBody mediaSeriesCreateRequestDto: MediaSeriesCreateRequestDto
    ): MediaSeriesResponseDto {
        return this.mediaContentsService.createMediaSeries(
            id,
            mediaSeriesCreateRequestDto
        )
    }

    @DeleteMapping("/series/{id}")
    fun deleteMediaSeriesById(
        @PathVariable id: UUID
    ): MediaSeriesResponseDto {
        return this.mediaContentsService.deleteMediaSeriesById(id)
    }

    @GetMapping("/{id}")
    fun findMediaContentsById(
        @AuthenticationPrincipal userId: String,
        @RequestHeader(value = "profileId") profileId: String,
        @PathVariable id: UUID
    ): MediaContentsResponseDto {
        return this.mediaContentsService.findMediaContentsById(
            UUID.fromString(userId),
            UUID.fromString(profileId),
            id
        )
    }

    @PostMapping
    fun createMediaContents(
        @RequestBody mediaContentsCreateRequestDto: MediaContentsCreateRequestDto
    ): MediaContentsResponseDto {
        return this.mediaContentsService.createMediaContents(
            mediaContentsCreateRequestDto
        )
    }

    @DeleteMapping("/{id}")
    fun deleteMediaContentsById(
        @PathVariable id: UUID
    ): MediaContentsResponseDto {
        return this.mediaContentsService.deleteMediaContentsById(id)
    }
}

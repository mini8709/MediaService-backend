package com.mediaservice.web

import com.mediaservice.application.MediaSeriesService
import com.mediaservice.application.dto.media.MediaAllSeriesResponseDto
import com.mediaservice.application.dto.media.MediaDetailRequestDto
import com.mediaservice.application.dto.media.MediaDetailResponseDto
import com.mediaservice.application.dto.media.MediaSeriesResponseDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/media-series")
class MediaSeriesController(private val mediaSeriesService: MediaSeriesService) {
    @GetMapping("/{id}")
    fun findMediaSeriesById(@PathVariable id: UUID): MediaSeriesResponseDto {
        return this.mediaSeriesService.findMediaSeriesById(id)
    }

    @GetMapping("/all/{id}")
    fun findMediaAllSeriesById(@PathVariable id: UUID): MediaAllSeriesResponseDto {
        return this.mediaSeriesService.findMediaAllSeriesById(id)
    }

    @GetMapping("/detail")
    fun findDetail(@RequestBody mediaDetailRequestDto: MediaDetailRequestDto): MediaDetailResponseDto {
        return this.mediaSeriesService.findDetail(mediaDetailRequestDto)
    }
}

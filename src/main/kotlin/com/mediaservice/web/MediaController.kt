package com.mediaservice.web

import com.mediaservice.application.MediaService
import com.mediaservice.application.dto.MediaResponseDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/media")
class MediaController(private val mediaService: MediaService) {
    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): MediaResponseDto {
        return this.mediaService.findById(id)
    }
}

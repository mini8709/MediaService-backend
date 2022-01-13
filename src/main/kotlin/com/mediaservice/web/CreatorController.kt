package com.mediaservice.web

import com.mediaservice.application.CreatorService
import com.mediaservice.application.dto.media.CreatorCreateRequestDto
import com.mediaservice.application.dto.media.CreatorResponseDto
import com.mediaservice.application.dto.media.CreatorUpdateRequestDto
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/creator")
class CreatorController(private val creatorService: CreatorService) {
    @PostMapping("")
    fun create(
        @RequestBody creatorCreateRequestDto: CreatorCreateRequestDto
    ): CreatorResponseDto {
        return this.creatorService.create(creatorCreateRequestDto)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @RequestBody creatorUpdateRequestDto: CreatorUpdateRequestDto
    ): CreatorResponseDto {
        return this.creatorService.update(id, creatorUpdateRequestDto)
    }
}

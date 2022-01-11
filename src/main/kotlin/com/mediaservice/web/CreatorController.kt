package com.mediaservice.web

import com.mediaservice.application.CreatorService
import com.mediaservice.application.dto.CreatorCreateRequestDto
import com.mediaservice.application.dto.CreatorResponseDto
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/creator")
class CreatorController(private val creatorService: CreatorService) {
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    fun create(
        @RequestBody creatorCreateRequestDto: CreatorCreateRequestDto
    ): CreatorResponseDto {
        return this.creatorService.create(creatorCreateRequestDto)
    }
}

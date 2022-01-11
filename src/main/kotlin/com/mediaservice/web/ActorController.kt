package com.mediaservice.web

import com.mediaservice.application.ActorService
import com.mediaservice.application.dto.ActorCreateRequestDto
import com.mediaservice.application.dto.ActorResponseDto
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/actor")
class ActorController(private val actorService: ActorService) {
    @PostMapping("/actor")
    @PreAuthorize("hasRole('ADMIN')")
    fun create(
        @RequestBody actorCreateRequestDto: ActorCreateRequestDto
    ): ActorResponseDto {
        return this.actorService.create(actorCreateRequestDto)
    }
}

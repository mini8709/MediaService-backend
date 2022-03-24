package com.mediaservice.web

import com.mediaservice.application.ActorService
import com.mediaservice.application.dto.media.ActorCreateRequestDto
import com.mediaservice.application.dto.media.ActorResponseDto
import com.mediaservice.application.dto.media.ActorUpdateRequestDto
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/actors")
class ActorController(private val actorService: ActorService) {
    @PostMapping("")
    fun create(
        @RequestBody @Valid actorCreateRequestDto: ActorCreateRequestDto
    ): ActorResponseDto {
        return this.actorService.create(actorCreateRequestDto)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @RequestBody @Valid actorUpdateRequestDto: ActorUpdateRequestDto
    ): ActorResponseDto {
        return this.actorService.update(id, actorUpdateRequestDto)
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: UUID
    ): ActorResponseDto {
        return this.actorService.delete(id)
    }

    @GetMapping
    fun findAll(
        @AuthenticationPrincipal userId: String,
        @RequestHeader(value = "profileId") profileId: String
    ): List<ActorResponseDto> {
        return this.actorService.findAll(
            UUID.fromString(userId),
            UUID.fromString(profileId)
        )
    }

    @GetMapping("/search")
    fun searchByName(
        @RequestParam name: String
    ): List<ActorResponseDto> {
        return this.actorService.searchByName(name)
    }
}

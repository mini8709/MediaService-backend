package com.mediaservice.web

import com.mediaservice.application.CreatorService
import com.mediaservice.application.dto.media.CreatorCreateRequestDto
import com.mediaservice.application.dto.media.CreatorResponseDto
import com.mediaservice.application.dto.media.CreatorUpdateRequestDto
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
@RequestMapping("/api/v1/creators")
class CreatorController(private val creatorService: CreatorService) {
    @PostMapping("")
    fun create(
        @RequestBody @Valid creatorCreateRequestDto: CreatorCreateRequestDto
    ): CreatorResponseDto {
        return this.creatorService.create(creatorCreateRequestDto)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @RequestBody @Valid creatorUpdateRequestDto: CreatorUpdateRequestDto
    ): CreatorResponseDto {
        return this.creatorService.update(id, creatorUpdateRequestDto)
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: UUID
    ): CreatorResponseDto {
        return creatorService.delete(id)
    }

    @GetMapping
    fun findAll(
        @AuthenticationPrincipal userId: String,
        @RequestHeader(value = "profileId") profileId: String
    ): List<CreatorResponseDto> {
        return this.creatorService.findAll(
            UUID.fromString(userId),
            UUID.fromString(profileId)
        )
    }

    @GetMapping("/search")
    fun searchByName(
        @RequestParam name: String
    ): List<CreatorResponseDto> {
        return this.creatorService.searchByName(name)
    }
}

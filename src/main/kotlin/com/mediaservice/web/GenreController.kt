package com.mediaservice.web

import com.mediaservice.application.GenreService
import com.mediaservice.application.dto.GenreCreateRequestDto
import com.mediaservice.application.dto.GenreResponseDto
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/genre")
class GenreController(private val genreService: GenreService) {
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    fun create(
        @RequestBody genreCreateRequestDto: GenreCreateRequestDto
    ): GenreResponseDto {
        return this.genreService.create(genreCreateRequestDto)
    }
}

package com.mediaservice.web

import com.mediaservice.application.ProfileService
import com.mediaservice.application.dto.ProfileResponseDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/profiles")
class ProfileController(private val profileService: ProfileService) {
    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): ProfileResponseDto {
        return this.profileService.findById(id)
    }
}

package com.mediaservice.web

import com.mediaservice.application.WishContentService
import com.mediaservice.application.dto.media.WishContentRequestDto
import com.mediaservice.application.dto.media.WishContentResponseDto
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/wish-contents")
class WishContentController(private val wishContentService: WishContentService) {

    @GetMapping
    fun findWishContentByProfileId(
        @AuthenticationPrincipal userId: String,
        @RequestHeader(value = "profileId") profileId: String
    ): List<WishContentResponseDto> {
        return this.wishContentService.findByProfileId(UUID.fromString(userId), UUID.fromString(profileId))
    }
    @PostMapping
    fun createWishContent(
        @AuthenticationPrincipal userId: String,
        @RequestHeader(value = "profileId") profileId: String,
        @RequestBody wishContentRequestDto: WishContentRequestDto
    ): WishContentResponseDto {
        return this.wishContentService.createWishContent(
            UUID.fromString(userId),
            UUID.fromString(profileId),
            wishContentRequestDto
        )
    }
    @DeleteMapping
    fun deleteWishContent(
        @AuthenticationPrincipal userId: String,
        @RequestHeader(value = "profileId") profileId: String,
        @RequestBody wishContentRequestDto: WishContentRequestDto
    ): WishContentResponseDto {
        return this.wishContentService.deleteWishContent(
            UUID.fromString(userId),
            UUID.fromString(profileId),
            wishContentRequestDto
        )
    }
}

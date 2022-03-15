package com.mediaservice.application.dto.media

class MediaUpdateRequestDto(
    val name: String,
    val synopsis: String,
    val order: Int,
    val url: String,
    val thumbnail: String,
    val runningTime: Int
)

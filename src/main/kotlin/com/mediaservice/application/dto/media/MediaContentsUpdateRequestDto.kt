package com.mediaservice.application.dto.media

data class MediaContentsUpdateRequestDto(
    val title: String,
    val synopsis: String,
    val trailer: String,
    val thumbnail: String,
    val rate: String,
    val isSeries: Boolean
)

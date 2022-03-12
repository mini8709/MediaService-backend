package com.mediaservice.application.dto.media

data class MediaContentsCreateRequestDto(
    val title: String,
    val synopsis: String,
    val trailer: String,
    val thumbnail: String,
    val rate: String,
    val isSeries: Boolean,
    val mediaSeriesTitle: String,
    val genreList: List<GenreCreateRequestDto>?,
    val actorList: List<ActorCreateRequestDto>?,
    val creatorList: List<CreatorCreateRequestDto>?
)

package com.mediaservice.application.dto.media

import com.mediaservice.domain.Media
import com.mediaservice.domain.MediaAllSeries
import com.mediaservice.domain.MediaSeries

data class MediaDetailResponseDto(
    val mediaAllSeriesResponseDto: MediaAllSeriesResponseDto,
    val mediaSeriesList: List<MediaSeriesResponseDto>,
    val mediaList: List<MediaResponseDto>,
    val isLike: Boolean,
    val genreList: List<GenreResponseDto>?,
    val actorList: List<ActorResponseDto>?,
    val creatorList: List<CreatorResponseDto>?
) {
    companion object {
        fun from(
            mediaAllSeries: MediaAllSeries,
            mediaSeriesList: List<MediaSeries>,
            mediaList: List<Media>,
            isLike: Boolean
        ) = MediaDetailResponseDto(
            mediaAllSeriesResponseDto = MediaAllSeriesResponseDto.from(mediaAllSeries),
            mediaSeriesList = mediaSeriesList.map { MediaSeriesResponseDto.from(it) },
            mediaList = mediaList.map { MediaResponseDto.from(it) },
            isLike = isLike,
            genreList = mediaAllSeries.genreList!!.map { GenreResponseDto.from(it) },
            actorList = mediaAllSeries.actorList!!.map { ActorResponseDto.from(it) },
            creatorList = mediaAllSeries.creatorList!!.map { CreatorResponseDto.from(it) }
        )
    }
}

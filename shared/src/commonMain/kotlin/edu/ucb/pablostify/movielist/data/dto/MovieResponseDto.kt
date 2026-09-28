package edu.ucb.pablostify.movielist.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class MovieResponseDto(
    val results: List<MovieDto>
)
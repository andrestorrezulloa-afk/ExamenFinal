package edu.ucb.pablostify.movielist.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDto(
    val id: Int,
    val title: String,

    @SerialName("poster_path")
    val posterPath: String? = null,

    @SerialName("vote_average")
    val voteAverage: Float = 0f,

    @SerialName("genre_ids")
    val genreIds: List<Int> = emptyList()
)
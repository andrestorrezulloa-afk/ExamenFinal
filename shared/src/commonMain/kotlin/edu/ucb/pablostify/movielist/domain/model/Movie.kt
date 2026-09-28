package edu.ucb.pablostify.movielist.domain.model

data class Movie(
    val id: String,
    val title: String,
    val genres: List<String>,
    val posterUrl: String,
    val rating: Float
)

package edu.ucb.pablostify.moviedetail.presentation.viewmodel

import edu.ucb.pablostify.moviedetail.domain.model.MovieDetail

data class MovieDetailState(
    val movie: MovieDetail? = null,
    val review: String = "",
    val ratingText: String = "",
    val isLoading: Boolean = false,
    val message: String? = null
)

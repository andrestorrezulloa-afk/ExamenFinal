package edu.ucb.pablostify.moviedetail.presentation.viewmodel

sealed interface MovieDetailEvents {
    data class Load(val movieId: String) : MovieDetailEvents
    data class OnReviewChanged(val value: String) : MovieDetailEvents
    data class OnRatingChanged(val value: String) : MovieDetailEvents
    data object SubmitReview : MovieDetailEvents
    data object Back : MovieDetailEvents
}

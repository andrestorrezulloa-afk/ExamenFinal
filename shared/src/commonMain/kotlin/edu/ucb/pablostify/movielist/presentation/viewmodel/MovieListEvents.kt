package edu.ucb.pablostify.movielist.presentation.viewmodel

sealed interface MovieListEvents {
    data object Load : MovieListEvents
    data class OnSearchChanged(val value: String) : MovieListEvents
    data class OnMovieSelected(val movieId: String) : MovieListEvents
    data object OnProfile : MovieListEvents
}

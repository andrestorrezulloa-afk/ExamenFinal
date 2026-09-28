package edu.ucb.pablostify.movielist.presentation.viewmodel

sealed interface MovieListEffects {
    data class NavigateToDetail(val movieId: String) : MovieListEffects
    data object NavigateToProfile : MovieListEffects
    data class ShowMessage(val message: String) : MovieListEffects
}

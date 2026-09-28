package edu.ucb.pablostify.moviedetail.presentation.viewmodel

sealed interface MovieDetailEffects {
    data object NavigateBack : MovieDetailEffects
    data class ShowMessage(val message: String) : MovieDetailEffects
}

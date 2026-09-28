package edu.ucb.pablostify.register.presentation.viewmodel

sealed interface RegisterEffects {
    data object NavigateToMovieList : RegisterEffects
    data object NavigateToLogin : RegisterEffects
    data class ShowMessage(val message: String) : RegisterEffects
}

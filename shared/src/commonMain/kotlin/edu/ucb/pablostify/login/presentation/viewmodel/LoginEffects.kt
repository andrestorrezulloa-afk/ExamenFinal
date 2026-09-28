package edu.ucb.pablostify.login.presentation.viewmodel

sealed interface LoginEffects {
    data object NavigateToMovieList : LoginEffects
    data object NavigateToRegister : LoginEffects
    data class ShowMessage(val message: String) : LoginEffects
}

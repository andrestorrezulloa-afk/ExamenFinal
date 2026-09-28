package edu.ucb.pablostify.profile.presentation.viewmodel

sealed interface ProfileEffects {
    data object NavigateToLogin : ProfileEffects
    data object NavigateToMovies : ProfileEffects
    data object NavigateToGithubLookup : ProfileEffects
    data class ShowMessage(val message: String) : ProfileEffects
}

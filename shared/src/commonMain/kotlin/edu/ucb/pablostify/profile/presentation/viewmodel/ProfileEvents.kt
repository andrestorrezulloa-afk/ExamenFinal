package edu.ucb.pablostify.profile.presentation.viewmodel

sealed interface ProfileEvents {
    data object Load : ProfileEvents
    data object Logout : ProfileEvents
    data object OpenMovies : ProfileEvents
    data object OpenGithubLookup : ProfileEvents
}

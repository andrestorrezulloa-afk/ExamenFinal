package edu.ucb.pablostify.login.presentation.viewmodel

sealed interface LoginEvents {
    data class OnUsernameChanged(val value: String) : LoginEvents
    data class OnPasswordChanged(val value: String) : LoginEvents
    data object OnSubmit : LoginEvents
    data object OnRegister : LoginEvents
}

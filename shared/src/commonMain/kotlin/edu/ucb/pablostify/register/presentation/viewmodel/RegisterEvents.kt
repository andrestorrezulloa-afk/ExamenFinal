package edu.ucb.pablostify.register.presentation.viewmodel

sealed interface RegisterEvents {
    data class OnFullNameChanged(val value: String) : RegisterEvents
    data class OnEmailChanged(val value: String) : RegisterEvents
    data class OnPasswordChanged(val value: String) : RegisterEvents
    data object OnSubmit : RegisterEvents
    data object OnLogin : RegisterEvents
}

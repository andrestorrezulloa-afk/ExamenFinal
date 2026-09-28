package edu.ucb.pablostify.login.presentation.viewmodel

data class LoginState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

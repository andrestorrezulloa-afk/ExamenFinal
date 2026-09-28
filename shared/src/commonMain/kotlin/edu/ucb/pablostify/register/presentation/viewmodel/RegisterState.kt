package edu.ucb.pablostify.register.presentation.viewmodel

data class RegisterState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

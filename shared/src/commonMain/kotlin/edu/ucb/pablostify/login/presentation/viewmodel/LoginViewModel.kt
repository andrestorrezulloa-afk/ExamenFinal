package edu.ucb.pablostify.login.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucb.pablostify.login.domain.model.Credentials
import edu.ucb.pablostify.login.domain.usecase.LoginUseCase
import edu.ucb.pablostify.login.domain.valueobject.Password
import edu.ucb.pablostify.login.domain.valueobject.Username
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<LoginEffects>()
    val effects = _effects.asSharedFlow()

    fun emitEvent(event: LoginEvents) {
        when (event) {
            is LoginEvents.OnUsernameChanged -> _state.update { it.copy(username = event.value, errorMessage = null) }
            is LoginEvents.OnPasswordChanged -> _state.update { it.copy(password = event.value, errorMessage = null) }
            LoginEvents.OnRegister -> emitEffect(LoginEffects.NavigateToRegister)
            LoginEvents.OnSubmit -> submit()
        }
    }

    private fun submit() {
        val current = _state.value
        val credentials = try {
            Credentials(Username(current.username.trim()), Password(current.password))
        } catch (e: IllegalArgumentException) {
            val message = e.message ?: "Datos inválidos"
            _state.update { it.copy(errorMessage = message) }
            emitEffect(LoginEffects.ShowMessage(message))
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            loginUseCase(credentials).fold(
                onSuccess = {
                    _state.update { it.copy(isLoading = false) }
                    emitEffect(LoginEffects.NavigateToMovieList)
                },
                onFailure = { error ->
                    val message = error.message ?: "Error al iniciar sesión"
                    _state.update { it.copy(isLoading = false, errorMessage = message) }
                    emitEffect(LoginEffects.ShowMessage(message))
                }
            )
        }
    }

    private fun emitEffect(effect: LoginEffects) {
        viewModelScope.launch { _effects.emit(effect) }
    }
}

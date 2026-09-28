package edu.ucb.pablostify.register.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucb.pablostify.register.domain.model.NewAccount
import edu.ucb.pablostify.register.domain.usecase.RegisterUseCase
import edu.ucb.pablostify.register.domain.valueobject.Email
import edu.ucb.pablostify.register.domain.valueobject.Password
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(private val registerUseCase: RegisterUseCase) : ViewModel() {
    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()
    private val _effects = MutableSharedFlow<RegisterEffects>()
    val effects = _effects.asSharedFlow()

    fun emitEvent(event: RegisterEvents) {
        when (event) {
            is RegisterEvents.OnFullNameChanged -> _state.update { it.copy(fullName = event.value, errorMessage = null) }
            is RegisterEvents.OnEmailChanged -> _state.update { it.copy(email = event.value, errorMessage = null) }
            is RegisterEvents.OnPasswordChanged -> _state.update { it.copy(password = event.value, errorMessage = null) }
            RegisterEvents.OnLogin -> emitEffect(RegisterEffects.NavigateToLogin)
            RegisterEvents.OnSubmit -> submit()
        }
    }

    private fun submit() {
        val current = _state.value
        if (current.fullName.isBlank()) {
            val message = "El nombre es obligatorio"
            _state.update { it.copy(errorMessage = message) }
            emitEffect(RegisterEffects.ShowMessage(message))
            return
        }
        val account = try {
            NewAccount(current.fullName.trim(), Email(current.email.trim()), Password(current.password))
        } catch (e: IllegalArgumentException) {
            val message = e.message ?: "Datos inválidos"
            _state.update { it.copy(errorMessage = message) }
            emitEffect(RegisterEffects.ShowMessage(message))
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            registerUseCase(account).fold(
                onSuccess = {
                    _state.update { it.copy(isLoading = false) }
                    emitEffect(RegisterEffects.NavigateToMovieList)
                },
                onFailure = { error ->
                    val message = error.message ?: "No se pudo registrar"
                    _state.update { it.copy(isLoading = false, errorMessage = message) }
                    emitEffect(RegisterEffects.ShowMessage(message))
                }
            )
        }
    }

    private fun emitEffect(effect: RegisterEffects) {
        viewModelScope.launch { _effects.emit(effect) }
    }
}

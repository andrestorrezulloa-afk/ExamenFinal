package edu.ucb.pablostify.profile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucb.pablostify.profile.domain.usecase.GetProfileUseCase
import edu.ucb.pablostify.profile.domain.usecase.LogoutUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()
    private val _effects = MutableSharedFlow<ProfileEffects>()
    val effects = _effects.asSharedFlow()

    init { load() }

    fun emitEvent(event: ProfileEvents) {
        when (event) {
            ProfileEvents.Load -> load()
            ProfileEvents.Logout -> logout()
            ProfileEvents.OpenMovies -> emitEffect(ProfileEffects.NavigateToMovies)
            ProfileEvents.OpenGithubLookup -> emitEffect(ProfileEffects.NavigateToGithubLookup)
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            getProfileUseCase().fold(
                onSuccess = { profile -> _state.update { it.copy(isLoading = false, profile = profile) } },
                onFailure = { error -> _state.update { it.copy(isLoading = false, errorMessage = error.message) } }
            )
        }
    }

    private fun logout() {
        viewModelScope.launch {
            logoutUseCase().fold(
                onSuccess = { emitEffect(ProfileEffects.NavigateToLogin) },
                onFailure = { emitEffect(ProfileEffects.ShowMessage(it.message ?: "No se pudo cerrar sesión")) }
            )
        }
    }

    private fun emitEffect(effect: ProfileEffects) { viewModelScope.launch { _effects.emit(effect) } }
}

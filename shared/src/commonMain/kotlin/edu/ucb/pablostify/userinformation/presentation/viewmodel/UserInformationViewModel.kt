package edu.ucb.pablostify.userinformation.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucb.pablostify.userinformation.domain.usecase.FindAliasUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserInformationViewModel(private val findAliasUseCase: FindAliasUseCase) : ViewModel() {
    private val _state = MutableStateFlow(UserInformationState())
    val state = _state.asStateFlow()
    private val _effects = MutableSharedFlow<UserInformationEffects>()
    val effects = _effects.asSharedFlow()

    fun emitEvent(event: UserInformationEvents) {
        when (event) {
            is UserInformationEvents.OnAliasChanged -> _state.update { it.copy(alias = event.value, errorMessage = null) }
            UserInformationEvents.OnBack -> emitEffect(UserInformationEffects.NavigateBack)
            UserInformationEvents.OnSubmit -> search()
        }
    }

    private fun search() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, user = null, errorMessage = null) }
            findAliasUseCase(_state.value.alias).fold(
                onSuccess = { user -> _state.update { it.copy(isLoading = false, user = user) } },
                onFailure = { error ->
                    val message = error.message ?: "No se pudo consultar GitHub"
                    _state.update { it.copy(isLoading = false, errorMessage = message) }
                    emitEffect(UserInformationEffects.ShowMessage(message))
                }
            )
        }
    }

    private fun emitEffect(effect: UserInformationEffects) { viewModelScope.launch { _effects.emit(effect) } }
}

package edu.ucb.pablostify.openmeteo.presentation.viewmodel

sealed interface OpenMeteoEffects {
    data object NavigateBack : OpenMeteoEffects
    data class ShowMessage(val message: String) : OpenMeteoEffects
}

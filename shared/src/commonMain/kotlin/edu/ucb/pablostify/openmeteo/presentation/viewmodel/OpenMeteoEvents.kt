package edu.ucb.pablostify.openmeteo.presentation.viewmodel

sealed interface OpenMeteoEvents {
    data class OnLatitudeChanged(val latitude: String) : OpenMeteoEvents
    data class OnLongitudeChanged(val longitude: String) : OpenMeteoEvents
    data object OnFetchWeather : OpenMeteoEvents
    data object OnBack : OpenMeteoEvents
}

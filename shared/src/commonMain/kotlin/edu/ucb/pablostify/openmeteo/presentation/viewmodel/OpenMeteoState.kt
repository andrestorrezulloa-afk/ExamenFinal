package edu.ucb.pablostify.openmeteo.presentation.viewmodel

import edu.ucb.pablostify.openmeteo.domain.model.WeatherInfo

data class OpenMeteoState(
    val latitudeText: String = "40.71",
    val longitudeText: String = "-74.01",
    val isLoading: Boolean = false,
    val weatherInfo: WeatherInfo? = null,
    val errorMessage: String? = null
)

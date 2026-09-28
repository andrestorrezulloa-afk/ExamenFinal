package edu.ucb.pablostify.openmeteo.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherDto(
    val temperature: Double? = null,
    val windspeed: Double? = null,
    val winddirection: Double? = null,
    val weathercode: Int? = null,
    val time: String? = null
)

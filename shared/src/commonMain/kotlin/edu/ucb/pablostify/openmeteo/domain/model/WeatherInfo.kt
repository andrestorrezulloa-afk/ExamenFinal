package edu.ucb.pablostify.openmeteo.domain.model

data class WeatherInfo(
    val temperature: Double,
    val windspeed: Double,
    val winddirection: Double,
    val weathercode: Int,
    val time: String,
    val latitude: Double,
    val longitude: Double
)

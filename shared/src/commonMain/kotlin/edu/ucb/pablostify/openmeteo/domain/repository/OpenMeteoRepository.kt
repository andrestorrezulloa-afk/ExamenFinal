package edu.ucb.pablostify.openmeteo.domain.repository

import edu.ucb.pablostify.openmeteo.domain.model.WeatherInfo

interface OpenMeteoRepository {
    suspend fun getWeather(latitude: Double, longitude: Double): Result<WeatherInfo>
}

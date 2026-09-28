package edu.ucb.pablostify.openmeteo.data.datasource

import edu.ucb.pablostify.openmeteo.data.dto.WeatherResponseDto

interface OpenMeteoRemoteDataSource {
    suspend fun getWeather(latitude: Double, longitude: Double): WeatherResponseDto
}

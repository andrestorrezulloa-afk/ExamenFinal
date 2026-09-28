package edu.ucb.pablostify.openmeteo.domain.usecase

import edu.ucb.pablostify.openmeteo.domain.model.WeatherInfo
import edu.ucb.pablostify.openmeteo.domain.repository.OpenMeteoRepository

class GetWeatherUseCase(
    private val repository: OpenMeteoRepository
) {
    suspend operator fun invoke(latitude: Double, longitude: Double): Result<WeatherInfo> {
        return repository.getWeather(latitude, longitude)
    }
}

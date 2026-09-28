package edu.ucb.pablostify.openmeteo.data.mapper

import edu.ucb.pablostify.openmeteo.data.dto.WeatherResponseDto
import edu.ucb.pablostify.openmeteo.domain.model.WeatherInfo

fun WeatherResponseDto.toModel(): WeatherInfo {
    val current = currentWeather
    return WeatherInfo(
        temperature = current?.temperature ?: 0.0,
        windspeed = current?.windspeed ?: 0.0,
        winddirection = current?.winddirection ?: 0.0,
        weathercode = current?.weathercode ?: 0,
        time = current?.time.orEmpty(),
        latitude = latitude ?: 0.0,
        longitude = longitude ?: 0.0
    )
}

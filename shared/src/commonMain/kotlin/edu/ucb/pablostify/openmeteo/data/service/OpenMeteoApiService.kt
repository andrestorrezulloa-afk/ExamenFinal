package edu.ucb.pablostify.openmeteo.data.service

import edu.ucb.pablostify.openmeteo.data.datasource.OpenMeteoRemoteDataSource
import edu.ucb.pablostify.openmeteo.data.dto.WeatherResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class OpenMeteoApiService : OpenMeteoRemoteDataSource {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    override suspend fun getWeather(latitude: Double, longitude: Double): WeatherResponseDto {
        return client.get("https://api.open-meteo.com/v1/forecast") {
            parameter("latitude", latitude)
            parameter("longitude", longitude)
            parameter("current_weather", true)
        }.body()
    }
}

package edu.ucb.pablostify

import edu.ucb.pablostify.openmeteo.data.dto.WeatherResponseDto
import edu.ucb.pablostify.openmeteo.data.mapper.toModel
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class WeatherIntegrationTest {

    @Test
    fun testWeatherResponseIntegration() {
        val json = """
            {
                "latitude": -17.39,
                "longitude": -66.15,
                "current_weather": {
                    "temperature": 25.0,
                    "windspeed": 10.5,
                    "winddirection": 180.0,
                    "weathercode": 0,
                    "time": "2026-11-20T10:00"
                }
            }
        """.trimIndent()

        val dto = Json.decodeFromString<WeatherResponseDto>(json)

        val weather = dto.toModel()

        assertEquals(25.0, weather.temperature)
        assertEquals(10.5, weather.windspeed)
        assertEquals(180.0, weather.winddirection)
        assertEquals(0, weather.weathercode)
        assertEquals("2026-11-20T10:00", weather.time)
        assertEquals(-17.39, weather.latitude)
        assertEquals(-66.15, weather.longitude)
    }
}

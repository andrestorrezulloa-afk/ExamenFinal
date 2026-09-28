package edu.ucb.pablostify.openmeteo.data.repository

import edu.ucb.pablostify.openmeteo.data.datasource.OpenMeteoRemoteDataSource
import edu.ucb.pablostify.openmeteo.data.mapper.toModel
import edu.ucb.pablostify.openmeteo.domain.model.WeatherInfo
import edu.ucb.pablostify.openmeteo.domain.repository.OpenMeteoRepository
import kotlinx.coroutines.CancellationException

class OpenMeteoRepositoryImpl(
    private val remoteDataSource: OpenMeteoRemoteDataSource
) : OpenMeteoRepository {

    override suspend fun getWeather(latitude: Double, longitude: Double): Result<WeatherInfo> {
        return try {
            val response = remoteDataSource.getWeather(latitude, longitude)
            Result.success(response.toModel())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

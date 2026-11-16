package edu.ucb.pablostify.openmeteo.data.repository

import edu.ucb.pablostify.openmeteo.data.datasource.RealTimeDataBase
import edu.ucb.pablostify.openmeteo.domain.repository.ExchangeRepository
import kotlinx.coroutines.flow.Flow

class ExchangeRepositoryImpl(
    private val realTimeDataBase: RealTimeDataBase
) : ExchangeRepository {
    override suspend fun observe(): Flow<String?> = realTimeDataBase.observeMessage()
}

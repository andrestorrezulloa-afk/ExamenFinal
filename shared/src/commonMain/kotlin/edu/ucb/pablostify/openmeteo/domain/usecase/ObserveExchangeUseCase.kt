package edu.ucb.pablostify.openmeteo.domain.usecase

import edu.ucb.pablostify.openmeteo.domain.repository.ExchangeRepository
import kotlinx.coroutines.flow.Flow

class ObserveExchangeUseCase(
    private val repository: ExchangeRepository
) {
    suspend fun invoke(): Flow<String?> = repository.observe()
}

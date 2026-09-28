package edu.ucb.pablostify.profile.domain.usecase

import edu.ucb.pablostify.profile.domain.repository.ProfileRepository

class LogoutUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(): Result<Unit> = repository.logout()
}

package edu.ucb.pablostify.profile.domain.usecase

import edu.ucb.pablostify.profile.domain.model.UserProfile
import edu.ucb.pablostify.profile.domain.repository.ProfileRepository

class GetProfileUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(): Result<UserProfile> = repository.getProfile()
}

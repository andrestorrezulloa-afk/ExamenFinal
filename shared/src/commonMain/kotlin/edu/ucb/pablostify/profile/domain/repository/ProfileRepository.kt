package edu.ucb.pablostify.profile.domain.repository

import edu.ucb.pablostify.profile.domain.model.UserProfile

interface ProfileRepository {
    suspend fun getProfile(): Result<UserProfile>
    suspend fun logout(): Result<Unit>
}

package edu.ucb.pablostify.profile.data

import edu.ucb.pablostify.core.domain.model.User
import edu.ucb.pablostify.profile.domain.model.UserProfile
import edu.ucb.pablostify.profile.domain.repository.ProfileRepository
import edu.ucb.pablostify.profile.domain.valueobject.SessionStatus

class ProfileRepositoryImpl : ProfileRepository {
    override suspend fun getProfile(): Result<UserProfile> = Result.success(
        UserProfile(
            user = User("1", "Ana García", "anagarcia@gmail.com", null),
            favoriteMovieIds = listOf("matrix", "spiderman", "interstellar"),
            sessionStatus = SessionStatus.LOGGED_IN
        )
    )

    override suspend fun logout(): Result<Unit> = Result.success(Unit)
}

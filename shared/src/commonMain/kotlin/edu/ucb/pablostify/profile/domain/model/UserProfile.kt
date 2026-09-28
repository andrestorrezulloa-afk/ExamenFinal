package edu.ucb.pablostify.profile.domain.model

import edu.ucb.pablostify.core.domain.model.User
import edu.ucb.pablostify.profile.domain.valueobject.SessionStatus

data class UserProfile(
    val user: User,
    val favoriteMovieIds: List<String>,
    val sessionStatus: SessionStatus
)

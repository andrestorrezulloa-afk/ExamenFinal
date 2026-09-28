package edu.ucb.pablostify.login.domain.repository

import edu.ucb.pablostify.core.domain.model.User
import edu.ucb.pablostify.login.domain.model.Credentials

interface LoginRepository {
    suspend fun login(credentials: Credentials): Result<User>
}

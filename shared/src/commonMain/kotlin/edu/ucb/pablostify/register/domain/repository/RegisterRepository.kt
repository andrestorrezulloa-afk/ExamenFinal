package edu.ucb.pablostify.register.domain.repository

import edu.ucb.pablostify.core.domain.model.User
import edu.ucb.pablostify.register.domain.model.NewAccount

interface RegisterRepository {
    suspend fun register(account: NewAccount): Result<User>
}

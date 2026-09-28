package edu.ucb.pablostify.register.data

import edu.ucb.pablostify.core.domain.model.User
import edu.ucb.pablostify.register.domain.model.NewAccount
import edu.ucb.pablostify.register.domain.repository.RegisterRepository

class RegisterRepositoryImpl : RegisterRepository {
    override suspend fun register(account: NewAccount): Result<User> = Result.success(
        User(
            id = "2",
            fullName = account.fullName,
            email = account.email.value,
            avatarUrl = null
        )
    )
}

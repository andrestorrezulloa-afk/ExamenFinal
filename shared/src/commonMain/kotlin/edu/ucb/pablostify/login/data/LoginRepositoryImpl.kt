package edu.ucb.pablostify.login.data

import edu.ucb.pablostify.core.domain.model.User
import edu.ucb.pablostify.login.domain.model.Credentials
import edu.ucb.pablostify.login.domain.repository.LoginRepository

class LoginRepositoryImpl : LoginRepository {
    override suspend fun login(credentials: Credentials): Result<User> = Result.success(
        User(
            id = "1",
            fullName = "Ana García",
            email = "anagarcia@gmail.com",
            avatarUrl = null
        )
    )
}

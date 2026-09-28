package edu.ucb.pablostify.login.domain.usecase

import edu.ucb.pablostify.core.domain.model.User
import edu.ucb.pablostify.login.domain.model.Credentials
import edu.ucb.pablostify.login.domain.repository.LoginRepository

class LoginUseCase(private val repository: LoginRepository) {
    suspend operator fun invoke(credentials: Credentials): Result<User> = repository.login(credentials)
}

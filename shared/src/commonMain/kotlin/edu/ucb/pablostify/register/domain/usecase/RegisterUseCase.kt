package edu.ucb.pablostify.register.domain.usecase

import edu.ucb.pablostify.core.domain.model.User
import edu.ucb.pablostify.register.domain.model.NewAccount
import edu.ucb.pablostify.register.domain.repository.RegisterRepository

class RegisterUseCase(private val repository: RegisterRepository) {
    suspend operator fun invoke(account: NewAccount): Result<User> = repository.register(account)
}

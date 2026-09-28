package edu.ucb.pablostify.userinformation.domain.usecase

import edu.ucb.pablostify.userinformation.domain.model.UserInfoModel
import edu.ucb.pablostify.userinformation.domain.repository.GithubRepository

class FindAliasUseCase(private val repository: GithubRepository) {
    suspend operator fun invoke(alias: String): Result<UserInfoModel> = repository.findByAlias(alias)
}

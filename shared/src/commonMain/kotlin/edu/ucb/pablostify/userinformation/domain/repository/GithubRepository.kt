package edu.ucb.pablostify.userinformation.domain.repository

import edu.ucb.pablostify.userinformation.domain.model.UserInfoModel

interface GithubRepository {
    suspend fun findByAlias(alias: String): Result<UserInfoModel>
}

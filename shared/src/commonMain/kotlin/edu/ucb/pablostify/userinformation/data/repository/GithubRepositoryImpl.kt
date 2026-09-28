package edu.ucb.pablostify.userinformation.data.repository

import edu.ucb.pablostify.userinformation.data.datasource.GithubRemoteDataSource
import edu.ucb.pablostify.userinformation.data.mapper.toDomain
import edu.ucb.pablostify.userinformation.domain.model.UserInfoModel
import edu.ucb.pablostify.userinformation.domain.repository.GithubRepository

class GithubRepositoryImpl(private val dataSource: GithubRemoteDataSource) : GithubRepository {
    override suspend fun findByAlias(alias: String): Result<UserInfoModel> {
        val cleanAlias = alias.trim()
        if (cleanAlias.isBlank()) return Result.failure(IllegalArgumentException("El alias es obligatorio"))
        return runCatching { dataSource.getUser(cleanAlias).toDomain(cleanAlias) }
    }
}

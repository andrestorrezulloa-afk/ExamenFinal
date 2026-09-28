package edu.ucb.pablostify.userinformation.data.datasource

import edu.ucb.pablostify.userinformation.data.dto.UserInfoDto

interface GithubRemoteDataSource {
    suspend fun getUser(nickname: String): UserInfoDto
}

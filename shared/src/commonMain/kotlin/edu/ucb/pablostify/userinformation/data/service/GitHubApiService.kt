package edu.ucb.pablostify.userinformation.data.service

import edu.ucb.pablostify.userinformation.data.datasource.GithubRemoteDataSource
import edu.ucb.pablostify.userinformation.data.dto.UserInfoDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class GitHubApiService : GithubRemoteDataSource {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    override suspend fun getUser(nickname: String): UserInfoDto {
        return client.get("https://api.github.com/users/${nickname.trim()}") {
            header(HttpHeaders.Accept, "application/vnd.github+json")
            header(HttpHeaders.UserAgent, "Pablostify-KMP")
        }.body()
    }
}

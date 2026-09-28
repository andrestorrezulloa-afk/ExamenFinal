package edu.ucb.pablostify.movielist.data

import edu.ucb.pablostify.movielist.data.dto.MovieDto
import edu.ucb.pablostify.movielist.data.dto.MovieResponseDto
import edu.ucb.pablostify.movielist.data.mapper.toDomain
import edu.ucb.pablostify.movielist.domain.model.Movie
import edu.ucb.pablostify.movielist.domain.repository.MovieRepository
import edu.ucb.pablostify.movielist.domain.valueobject.MovieFilter
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class MovieRepositoryImpl : MovieRepository {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }
    }

    override suspend fun getPopularMovies(
        filter: MovieFilter
    ): Result<List<Movie>> {

        return try {

            val response = client.get(
                "https://api.themoviedb.org/3/discover/movie" +
                        "?sort_by=popularity.desc" +
                        "&api_key=fa3e844ce31744388e07fa47c7c5d8c3"
            )

            val movieResponse: MovieResponseDto =
                response.body<MovieResponseDto>()

            var movies: List<Movie> =
                movieResponse.results.map { dto: MovieDto ->
                    dto.toDomain()
                }

            if (filter.query.isNotBlank()) {
                movies = movies.filter { movie: Movie ->
                    movie.title.contains(
                        filter.query,
                        ignoreCase = true
                    )
                }
            }

            Result.success(movies)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
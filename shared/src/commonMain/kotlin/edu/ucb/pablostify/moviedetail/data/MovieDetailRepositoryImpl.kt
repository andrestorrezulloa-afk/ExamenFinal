package edu.ucb.pablostify.moviedetail.data

import edu.ucb.pablostify.moviedetail.data.dto.CreditsDto
import edu.ucb.pablostify.moviedetail.data.dto.MovieDetailDto
import edu.ucb.pablostify.moviedetail.domain.model.CastMember
import edu.ucb.pablostify.moviedetail.domain.model.MovieDetail
import edu.ucb.pablostify.moviedetail.domain.repository.MovieDetailRepository
import edu.ucb.pablostify.moviedetail.domain.valueobject.Rating
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class MovieDetailRepositoryImpl : MovieDetailRepository {

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

    override suspend fun getMovieDetail(
        movieId: String
    ): Result<MovieDetail> {

        return try {

            val detailResponse = client.get(
                "https://api.themoviedb.org/3/movie/$movieId" +
                        "?api_key=fa3e844ce31744388e07fa47c7c5d8c3" +
                        "&language=es-ES"
            )

            val creditsResponse = client.get(
                "https://api.themoviedb.org/3/movie/$movieId/credits" +
                        "?api_key=fa3e844ce31744388e07fa47c7c5d8c3" +
                        "&language=es-ES"
            )

            val detail = detailResponse.body<MovieDetailDto>()
            val credits = creditsResponse.body<CreditsDto>()

            val posterUrl =
                if (detail.posterPath != null) {
                    "https://image.tmdb.org/t/p/w500${detail.posterPath}"
                } else {
                    ""
                }

            val cast = credits.cast
                .take(10)
                .map { actor ->

                    val profileUrl =
                        if (actor.profilePath != null) {
                            "https://image.tmdb.org/t/p/w500${actor.profilePath}"
                        } else {
                            ""
                        }

                    CastMember(
                        actor.id.toString(),
                        actor.name,
                        profileUrl
                    )
                }

            val movie = MovieDetail(
                detail.id.toString(),
                detail.title,
                detail.overview,
                posterUrl,
                cast,
                Rating(detail.voteAverage)
            )

            Result.success(movie)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun submitReview(
        movieId: String,
        review: String,
        rating: Float
    ): Result<Unit> {

        if (review.isBlank()) {
            return Result.failure(
                IllegalArgumentException(
                    "La reseña no puede estar vacía"
                )
            )
        }

        return try {
            Rating(rating)
            Result.success(Unit)
        } catch (e: IllegalArgumentException) {
            Result.failure(e)
        }
    }
}
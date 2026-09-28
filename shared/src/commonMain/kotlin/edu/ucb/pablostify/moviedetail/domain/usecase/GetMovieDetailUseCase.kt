package edu.ucb.pablostify.moviedetail.domain.usecase

import edu.ucb.pablostify.moviedetail.domain.model.MovieDetail
import edu.ucb.pablostify.moviedetail.domain.repository.MovieDetailRepository

class GetMovieDetailUseCase(private val repository: MovieDetailRepository) {
    suspend operator fun invoke(movieId: String): Result<MovieDetail> = repository.getMovieDetail(movieId)
}

package edu.ucb.pablostify.moviedetail.domain.usecase

import edu.ucb.pablostify.moviedetail.domain.repository.MovieDetailRepository

class SubmitReviewUseCase(private val repository: MovieDetailRepository) {
    suspend operator fun invoke(movieId: String, review: String, rating: Float): Result<Unit> = repository.submitReview(movieId, review, rating)
}

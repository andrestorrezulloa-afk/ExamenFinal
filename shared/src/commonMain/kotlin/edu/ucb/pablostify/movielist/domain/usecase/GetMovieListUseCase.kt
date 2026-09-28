package edu.ucb.pablostify.movielist.domain.usecase

import edu.ucb.pablostify.movielist.domain.model.Movie
import edu.ucb.pablostify.movielist.domain.repository.MovieRepository
import edu.ucb.pablostify.movielist.domain.valueobject.MovieFilter

class GetMovieListUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(filter: MovieFilter): Result<List<Movie>> = repository.getPopularMovies(filter)
}

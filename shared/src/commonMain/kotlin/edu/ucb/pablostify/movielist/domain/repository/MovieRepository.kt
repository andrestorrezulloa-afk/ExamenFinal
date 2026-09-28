package edu.ucb.pablostify.movielist.domain.repository

import edu.ucb.pablostify.movielist.domain.model.Movie
import edu.ucb.pablostify.movielist.domain.valueobject.MovieFilter

interface MovieRepository {
    suspend fun getPopularMovies(filter: MovieFilter): Result<List<Movie>>
}

package edu.ucb.pablostify.movielist.data.mapper

import edu.ucb.pablostify.movielist.data.dto.MovieDto
import edu.ucb.pablostify.movielist.domain.model.Movie

fun MovieDto.toDomain(): Movie {
    return Movie(
        id = id.toString(),
        title = title,
        genres = genreIds.map { it.toString() },
        posterUrl = if (posterPath != null) {
            "https://image.tmdb.org/t/p/w500$posterPath"
        } else {
            ""
        },
        rating = voteAverage
    )
}
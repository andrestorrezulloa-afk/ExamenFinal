package edu.ucb.pablostify.moviedetail.domain.model

import edu.ucb.pablostify.moviedetail.domain.valueobject.Rating

data class MovieDetail(
    val id: String,
    val title: String,
    val synopsis: String,
    val posterUrl: String,
    val cast: List<CastMember>,
    val rating: Rating
)

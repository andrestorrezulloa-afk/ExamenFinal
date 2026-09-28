package edu.ucb.pablostify.moviedetail.domain.valueobject

@JvmInline
value class Rating(val value: Float) {
    init { require(value in 0f..10f) { "El puntaje debe estar entre 0 y 10" } }
}

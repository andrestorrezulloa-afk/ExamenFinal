package edu.ucb.pablostify.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoute {
    @Serializable
    object Login : NavRoute()

    @Serializable
    object Register : NavRoute()

    @Serializable
    object MovieList : NavRoute()

    @Serializable
    data class MovieDetail(val movieId: String) : NavRoute()

    @Serializable
    object Profile : NavRoute()

    @Serializable
    object UserInformation : NavRoute()
}

package edu.ucb.pablostify.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import edu.ucb.pablostify.login.presentation.screen.LoginScreen
import edu.ucb.pablostify.moviedetail.presentation.screen.MovieDetailScreen
import edu.ucb.pablostify.movielist.presentation.screen.MovieListScreen
import edu.ucb.pablostify.profile.presentation.screen.ProfileScreen
import edu.ucb.pablostify.register.presentation.screen.RegisterScreen
import edu.ucb.pablostify.userinformation.presentation.screen.UserInformationScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavRoute.Login) {
        composable<NavRoute.Login> { LoginScreen(navController) }
        composable<NavRoute.Register> { RegisterScreen(navController) }
        composable<NavRoute.MovieList> { MovieListScreen(navController) }
        composable<NavRoute.MovieDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<NavRoute.MovieDetail>()
            MovieDetailScreen(navController, route.movieId)
        }
        composable<NavRoute.Profile> { ProfileScreen(navController) }
        composable<NavRoute.UserInformation> { UserInformationScreen(navController) }
    }
}

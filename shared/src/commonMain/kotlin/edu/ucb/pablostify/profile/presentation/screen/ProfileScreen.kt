package edu.ucb.pablostify.profile.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import edu.ucb.pablostify.navigation.NavRoute
import edu.ucb.pablostify.profile.presentation.viewmodel.ProfileEffects
import edu.ucb.pablostify.profile.presentation.viewmodel.ProfileEvents
import edu.ucb.pablostify.profile.presentation.viewmodel.ProfileViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(navController: NavHostController, viewModel: ProfileViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                ProfileEffects.NavigateToLogin -> navController.navigate(NavRoute.Login) { launchSingleTop = true }
                ProfileEffects.NavigateToMovies -> navController.navigate(NavRoute.MovieList) { launchSingleTop = true }
                ProfileEffects.NavigateToGithubLookup -> navController.navigate(NavRoute.UserInformation)
                is ProfileEffects.ShowMessage -> Unit
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Perfil")
        if (state.isLoading) Text("Cargando...")
        state.profile?.let { profile ->
            Text(profile.user.fullName, modifier = Modifier.padding(top = 12.dp))
            Text(profile.user.email)
            Text("Películas favoritas: ${profile.favoriteMovieIds.size}", modifier = Modifier.padding(top = 12.dp))
            profile.favoriteMovieIds.forEach { Text("• $it") }
        }
        state.errorMessage?.let { Text(it) }
        Button(onClick = { viewModel.emitEvent(ProfileEvents.OpenMovies) }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) { Text("Ver películas") }
        Button(onClick = { viewModel.emitEvent(ProfileEvents.OpenGithubLookup) }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) { Text("Consulta GitHub (Ktor)") }
        Button(onClick = { viewModel.emitEvent(ProfileEvents.Logout) }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) { Text("Cerrar sesión") }
    }
}

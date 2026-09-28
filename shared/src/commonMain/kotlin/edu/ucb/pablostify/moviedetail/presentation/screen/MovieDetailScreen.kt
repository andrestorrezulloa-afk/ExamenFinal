package edu.ucb.pablostify.moviedetail.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import edu.ucb.pablostify.moviedetail.presentation.viewmodel.MovieDetailEffects
import edu.ucb.pablostify.moviedetail.presentation.viewmodel.MovieDetailEvents
import edu.ucb.pablostify.moviedetail.presentation.viewmodel.MovieDetailViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MovieDetailScreen(
    navController: NavHostController,
    movieId: String,
    viewModel: MovieDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(movieId) { viewModel.emitEvent(MovieDetailEvents.Load(movieId)) }
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                MovieDetailEffects.NavigateBack -> navController.popBackStack()
                is MovieDetailEffects.ShowMessage -> Unit
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        Button(onClick = { viewModel.emitEvent(MovieDetailEvents.Back) }) { Text("Volver") }
        if (state.isLoading) Text("Cargando...")
        state.movie?.let { movie ->
            Text(movie.title, modifier = Modifier.padding(top = 16.dp))
            Text("Puntuación: ${movie.rating.value}/10")
            Text(movie.synopsis, modifier = Modifier.padding(vertical = 12.dp))
            Text("Reparto")
            movie.cast.forEach { Text("• ${it.name}") }
            Text("Escribir reseña", modifier = Modifier.padding(top = 20.dp))
            OutlinedTextField(state.review, { viewModel.emitEvent(MovieDetailEvents.OnReviewChanged(it)) }, label = { Text("Reseña") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(state.ratingText, { viewModel.emitEvent(MovieDetailEvents.OnRatingChanged(it)) }, label = { Text("Puntuación 0-10") }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
            Button(onClick = { viewModel.emitEvent(MovieDetailEvents.SubmitReview) }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) { Text("Enviar reseña") }
        }
        state.message?.let { Text(it, modifier = Modifier.padding(top = 8.dp)) }
    }
}

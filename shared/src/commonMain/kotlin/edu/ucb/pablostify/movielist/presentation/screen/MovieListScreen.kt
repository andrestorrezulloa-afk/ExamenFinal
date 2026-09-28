    package edu.ucb.pablostify.movielist.presentation.screen

    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.lazy.grid.GridCells
    import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
    import androidx.compose.foundation.lazy.grid.items
    import androidx.compose.foundation.layout.aspectRatio
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.text.style.TextAlign
    import androidx.compose.material3.Button
    import androidx.compose.material3.Card
    import androidx.compose.material3.OutlinedTextField
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.unit.dp
    import androidx.navigation.NavHostController
    import edu.ucb.pablostify.movielist.presentation.viewmodel.MovieListEffects
    import edu.ucb.pablostify.movielist.presentation.viewmodel.MovieListEvents
    import edu.ucb.pablostify.movielist.presentation.viewmodel.MovieListViewModel
    import edu.ucb.pablostify.navigation.NavRoute
    import androidx.compose.ui.tooling.preview.Preview
    import org.koin.compose.viewmodel.koinViewModel
    import androidx.compose.ui.layout.ContentScale
    import coil3.compose.AsyncImage

    @Composable
    fun MovieListScreen(
        navController: NavHostController,
        viewModel: MovieListViewModel = koinViewModel()
    ) {
        val state by viewModel.state.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.effects.collect { effect ->
                when (effect) {
                    is MovieListEffects.NavigateToDetail ->
                        navController.navigate(NavRoute.MovieDetail(effect.movieId))

                    MovieListEffects.NavigateToProfile ->
                        navController.navigate(NavRoute.Profile)

                    is MovieListEffects.ShowMessage -> Unit
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Películas populares")

                Button(
                    onClick = {
                        viewModel.emitEvent(MovieListEvents.OnProfile)
                    }
                ) {
                    Text("Perfil")
                }
            }

            OutlinedTextField(
                value = state.query,
                onValueChange = {
                    viewModel.emitEvent(
                        MovieListEvents.OnSearchChanged(it)
                    )
                },
                label = {
                    Text("Buscar película o género")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            )

            state.errorMessage?.let {
                Text(it)
            }

            if (state.isLoading) {
                Text("Cargando...")
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = state.movies,
                    key = { it.id }
                ) { movie ->

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.emitEvent(
                                    MovieListEvents.OnMovieSelected(movie.id)
                                )
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(0.67f)
                        ) {
                            AsyncImage(
                                model = movie.posterUrl,
                                contentDescription = movie.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Text(
                            text = movie.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    fun MovieListPreview() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Películas populares")

            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = {
                    Text("Buscar película o género")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            )

            Text("Puntuación: 8.5")
        }
    }

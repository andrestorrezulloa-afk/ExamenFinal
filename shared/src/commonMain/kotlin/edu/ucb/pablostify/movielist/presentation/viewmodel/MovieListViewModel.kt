package edu.ucb.pablostify.movielist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucb.pablostify.movielist.domain.usecase.GetMovieListUseCase
import edu.ucb.pablostify.movielist.domain.valueobject.MovieFilter
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieListViewModel(private val getMovieListUseCase: GetMovieListUseCase) : ViewModel() {
    private val _state = MutableStateFlow(MovieListState())
    val state = _state.asStateFlow()
    private val _effects = MutableSharedFlow<MovieListEffects>()
    val effects = _effects.asSharedFlow()

    init { load("") }

    fun emitEvent(event: MovieListEvents) {
        when (event) {
            MovieListEvents.Load -> load(_state.value.query)
            is MovieListEvents.OnSearchChanged -> {
                _state.update { it.copy(query = event.value) }
                load(event.value)
            }
            is MovieListEvents.OnMovieSelected -> emitEffect(MovieListEffects.NavigateToDetail(event.movieId))
            MovieListEvents.OnProfile -> emitEffect(MovieListEffects.NavigateToProfile)
        }
    }

    private fun load(query: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            getMovieListUseCase(MovieFilter(query)).fold(
                onSuccess = { movies -> _state.update { it.copy(isLoading = false, movies = movies) } },
                onFailure = { error ->
                    val message = error.message ?: "No se pudieron cargar las películas"
                    _state.update { it.copy(isLoading = false, errorMessage = message) }
                    emitEffect(MovieListEffects.ShowMessage(message))
                }
            )
        }
    }

    private fun emitEffect(effect: MovieListEffects) { viewModelScope.launch { _effects.emit(effect) } }
}

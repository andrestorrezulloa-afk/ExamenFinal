package edu.ucb.pablostify.moviedetail.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucb.pablostify.moviedetail.domain.usecase.GetMovieDetailUseCase
import edu.ucb.pablostify.moviedetail.domain.usecase.SubmitReviewUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val submitReviewUseCase: SubmitReviewUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(MovieDetailState())
    val state = _state.asStateFlow()
    private val _effects = MutableSharedFlow<MovieDetailEffects>()
    val effects = _effects.asSharedFlow()
    private var currentMovieId: String = ""

    fun emitEvent(event: MovieDetailEvents) {
        when (event) {
            is MovieDetailEvents.Load -> load(event.movieId)
            is MovieDetailEvents.OnReviewChanged -> _state.update { it.copy(review = event.value, message = null) }
            is MovieDetailEvents.OnRatingChanged -> _state.update { it.copy(ratingText = event.value, message = null) }
            MovieDetailEvents.SubmitReview -> submitReview()
            MovieDetailEvents.Back -> emitEffect(MovieDetailEffects.NavigateBack)
        }
    }

    private fun load(movieId: String) {
        currentMovieId = movieId
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, message = null) }
            getMovieDetailUseCase(movieId).fold(
                onSuccess = { movie -> _state.update { it.copy(isLoading = false, movie = movie) } },
                onFailure = { error -> _state.update { it.copy(isLoading = false, message = error.message ?: "Error") } }
            )
        }
    }

    private fun submitReview() {
        val rating = _state.value.ratingText.toFloatOrNull()
        if (rating == null) {
            val message = "Ingresa una puntuación entre 0 y 10"
            _state.update { it.copy(message = message) }
            emitEffect(MovieDetailEffects.ShowMessage(message))
            return
        }
        viewModelScope.launch {
            submitReviewUseCase(currentMovieId, _state.value.review, rating).fold(
                onSuccess = {
                    val message = "Reseña enviada correctamente"
                    _state.update { it.copy(review = "", ratingText = "", message = message) }
                    emitEffect(MovieDetailEffects.ShowMessage(message))
                },
                onFailure = { error ->
                    val message = error.message ?: "No se pudo enviar la reseña"
                    _state.update { it.copy(message = message) }
                    emitEffect(MovieDetailEffects.ShowMessage(message))
                }
            )
        }
    }

    private fun emitEffect(effect: MovieDetailEffects) { viewModelScope.launch { _effects.emit(effect) } }
}

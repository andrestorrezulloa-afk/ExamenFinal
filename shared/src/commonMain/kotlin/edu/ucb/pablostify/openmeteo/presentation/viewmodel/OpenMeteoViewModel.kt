package edu.ucb.pablostify.openmeteo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucb.pablostify.openmeteo.domain.usecase.GetWeatherUseCase
import edu.ucb.pablostify.openmeteo.domain.usecase.ObserveExchangeUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OpenMeteoViewModel(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val observeExchangeUseCase: ObserveExchangeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(OpenMeteoState())
    val state: StateFlow<OpenMeteoState> = _state.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    private val _firebaseError = MutableStateFlow<String?>(null)
    val firebaseError = _firebaseError.asStateFlow()

    private val _effects = Channel<OpenMeteoEffects>(Channel.BUFFERED)
    val effects: Flow<OpenMeteoEffects> = _effects.receiveAsFlow()

    init {
        fetchWeather()
        observeMessage()
    }

    private fun observeMessage() {
        viewModelScope.launch {
            try {
                observeExchangeUseCase.invoke()
                    .catch { error ->
                        _firebaseError.value = error.message ?: "No se pudo leer app_weather"
                    }
                    .collect { value ->
                        _message.value = value
                        _firebaseError.value = null
                    }
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _firebaseError.value = error.message ?: "No se pudo iniciar la lectura de app_weather"
            }
        }
    }

    fun emitEvent(event: OpenMeteoEvents) {
        when (event) {
            is OpenMeteoEvents.OnLatitudeChanged -> {
                _state.update { it.copy(latitudeText = event.latitude) }
            }
            is OpenMeteoEvents.OnLongitudeChanged -> {
                _state.update { it.copy(longitudeText = event.longitude) }
            }
            OpenMeteoEvents.OnFetchWeather -> {
                fetchWeather()
            }
            OpenMeteoEvents.OnBack -> {
                viewModelScope.launch {
                    _effects.send(OpenMeteoEffects.NavigateBack)
                }
            }
        }
    }

    private fun fetchWeather() {
        val lat = _state.value.latitudeText.toDoubleOrNull()
        val lon = _state.value.longitudeText.toDoubleOrNull()

        if (lat == null || lon == null) {
            _state.update { it.copy(errorMessage = "Ingrese valores numéricos válidos para latitud y longitud") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            getWeatherUseCase(lat, lon)
                .onSuccess { weather ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            weatherInfo = weather,
                            errorMessage = null
                        )
                    }
                }
                .onFailure { error ->
                    val msg = error.message ?: "Error al obtener el clima"
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = msg
                        )
                    }
                    _effects.send(OpenMeteoEffects.ShowMessage(msg))
                }
        }
    }
}

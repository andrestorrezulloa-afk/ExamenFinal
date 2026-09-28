package edu.ucb.pablostify.openmeteo.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import edu.ucb.pablostify.openmeteo.presentation.viewmodel.OpenMeteoEffects
import edu.ucb.pablostify.openmeteo.presentation.viewmodel.OpenMeteoEvents
import edu.ucb.pablostify.openmeteo.presentation.viewmodel.OpenMeteoViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OpenMeteoScreen(
    navController: NavHostController,
    viewModel: OpenMeteoViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                OpenMeteoEffects.NavigateBack -> navController.popBackStack()
                is OpenMeteoEffects.ShowMessage -> Unit
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = "Clima en Tiempo Real (Open-Meteo)",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Consulta el clima actual especificando latitud y longitud",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )

        OutlinedTextField(
            value = state.latitudeText,
            onValueChange = { viewModel.emitEvent(OpenMeteoEvents.OnLatitudeChanged(it)) },
            label = { Text("Latitud") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.longitudeText,
            onValueChange = { viewModel.emitEvent(OpenMeteoEvents.OnLongitudeChanged(it)) },
            label = { Text("Longitud") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { viewModel.emitEvent(OpenMeteoEvents.OnFetchWeather) },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (state.isLoading) "Consultando..." else "Consultar Clima")
        }

        state.errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp)
            )
        }

        state.weatherInfo?.let { weather ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Datos del Clima",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text("• Temperatura: ${weather.temperature} °C")
                    Text("• Velocidad del viento: ${weather.windspeed} km/h")
                    Text("• Dirección del viento: ${weather.winddirection}°")
                    Text("• Código de clima (weathercode): ${weather.weathercode}")
                    Text("• Fecha y hora: ${weather.time}")
                    Text("• Latitud: ${weather.latitude}")
                    Text("• Longitud: ${weather.longitude}")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.emitEvent(OpenMeteoEvents.OnBack) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}

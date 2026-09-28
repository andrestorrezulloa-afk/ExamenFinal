package edu.ucb.pablostify.register.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import edu.ucb.pablostify.navigation.NavRoute
import edu.ucb.pablostify.register.presentation.viewmodel.RegisterEffects
import edu.ucb.pablostify.register.presentation.viewmodel.RegisterEvents
import edu.ucb.pablostify.register.presentation.viewmodel.RegisterViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreen(navController: NavHostController, viewModel: RegisterViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                RegisterEffects.NavigateToLogin -> navController.popBackStack()
                RegisterEffects.NavigateToMovieList -> navController.navigate(NavRoute.MovieList) { launchSingleTop = true }
                is RegisterEffects.ShowMessage -> Unit
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Crear cuenta")
        OutlinedTextField(state.fullName, { viewModel.emitEvent(RegisterEvents.OnFullNameChanged(it)) }, label = { Text("Nombre completo") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(state.email, { viewModel.emitEvent(RegisterEvents.OnEmailChanged(it)) }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
        OutlinedTextField(state.password, { viewModel.emitEvent(RegisterEvents.OnPasswordChanged(it)) }, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
        state.errorMessage?.let { Text(it, modifier = Modifier.padding(top = 8.dp)) }
        Button(onClick = { viewModel.emitEvent(RegisterEvents.OnSubmit) }, enabled = !state.isLoading, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) { Text(if (state.isLoading) "Registrando..." else "Registrarse") }
        TextButton(onClick = { viewModel.emitEvent(RegisterEvents.OnLogin) }) { Text("Volver a iniciar sesión") }
    }
}

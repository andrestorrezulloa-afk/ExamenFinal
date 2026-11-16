package edu.ucb.pablostify.login.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import edu.ucb.pablostify.login.presentation.viewmodel.LoginEffects
import edu.ucb.pablostify.login.presentation.viewmodel.LoginEvents
import edu.ucb.pablostify.login.presentation.viewmodel.LoginViewModel
import edu.ucb.pablostify.navigation.NavRoute
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import pablostify.shared.generated.resources.Res
import pablostify.shared.generated.resources.login_button
import pablostify.shared.generated.resources.login_password
import pablostify.shared.generated.resources.login_register
import pablostify.shared.generated.resources.login_title

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                LoginEffects.NavigateToMovieList -> navController.navigate(NavRoute.MovieList) { launchSingleTop = true }
                LoginEffects.NavigateToRegister -> navController.navigate(NavRoute.Register)
                is LoginEffects.ShowMessage -> Unit
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Pablostify")
        Text(stringResource(Res.string.login_title), modifier = Modifier.padding(bottom = 16.dp))
        OutlinedTextField(
            value = state.username,
            onValueChange = { viewModel.emitEvent(LoginEvents.OnUsernameChanged(it)) },
            label = { Text("Usuario") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.password,
            onValueChange = { viewModel.emitEvent(LoginEvents.OnPasswordChanged(it)) },
            label = { Text(stringResource(Res.string.login_password)) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        state.errorMessage?.let { Text(it, modifier = Modifier.padding(top = 8.dp)) }
        Button(
            onClick = { viewModel.emitEvent(LoginEvents.OnSubmit) },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            if (state.isLoading) CircularProgressIndicator() else Text(stringResource(Res.string.login_button))
        }
        TextButton(onClick = { viewModel.emitEvent(LoginEvents.OnRegister) }) {
            Text(stringResource(Res.string.login_register))
        }
    }
}

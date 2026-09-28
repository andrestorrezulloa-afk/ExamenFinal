package edu.ucb.pablostify.userinformation.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import edu.ucb.pablostify.userinformation.presentation.viewmodel.UserInformationEffects
import edu.ucb.pablostify.userinformation.presentation.viewmodel.UserInformationEvents
import edu.ucb.pablostify.userinformation.presentation.viewmodel.UserInformationViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UserInformationScreen(
    navController: NavHostController,
    viewModel: UserInformationViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                UserInformationEffects.NavigateBack -> navController.popBackStack()
                is UserInformationEffects.ShowMessage -> Unit
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Consulta de usuario GitHub con Ktor")
        OutlinedTextField(
            value = state.alias,
            onValueChange = { viewModel.emitEvent(UserInformationEvents.OnAliasChanged(it)) },
            label = { Text("Alias de GitHub") },
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
        )
        Button(onClick = { viewModel.emitEvent(UserInformationEvents.OnSubmit) }, enabled = !state.isLoading, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            Text(if (state.isLoading) "Buscando..." else "Buscar")
        }
        state.errorMessage?.let { Text(it, modifier = Modifier.padding(top = 8.dp)) }
        state.user?.let { user ->
            Text("Alias: ${user.alias}", modifier = Modifier.padding(top = 16.dp))
            Text("Email: ${user.email.ifBlank { "No público" }}")
            Text("Empresa: ${user.company.ifBlank { "No pública" }}")
            Text("Avatar URL: ${user.avatarUrl.ifBlank { "No disponible" }}")
        }
        Button(onClick = { viewModel.emitEvent(UserInformationEvents.OnBack) }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) { Text("Volver") }
    }
}

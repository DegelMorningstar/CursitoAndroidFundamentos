package com.yaeldev.cursitodefundamentosandroid.feature.auth.presentation.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yaeldev.cursitodefundamentosandroid.feature.auth.data.remote.firebase.AuthRepositoryFirebase
import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.usecases.IniciarSesionUseCase

@Composable
fun LoginRoot(
    onAutenticado: () -> Unit,
    onIrARegistro: () -> Unit
) {
    val repository = remember { AuthRepositoryFirebase() }
    val factory = remember { LoginViewModelFactory(IniciarSesionUseCase(repository)) }
    val viewModel: LoginViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val actions = remember(viewModel) {
        LoginActions(
            onEmailChange = { nuevoValor -> viewModel.onEmailChange(nuevoValor) },
            onPasswordChange = { nuevoValor -> viewModel.onPasswordChange(nuevoValor) },
            onIniciarSesion = { viewModel.iniciarSesion(onAutenticado) },
            onIrARegistro = onIrARegistro
        )
    }

    LoginScreen(
        uiState = uiState,
        actions = actions
    )
}

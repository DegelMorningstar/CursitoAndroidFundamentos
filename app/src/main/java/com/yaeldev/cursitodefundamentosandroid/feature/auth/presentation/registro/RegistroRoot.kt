package com.yaeldev.cursitodefundamentosandroid.feature.auth.presentation.registro

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yaeldev.cursitodefundamentosandroid.feature.auth.data.remote.firebase.AuthRepositoryFirebase
import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.usecases.RegistrarUseCase

@Composable
fun RegistroRoot(
    onRegistrado: () -> Unit,
    onIrALogin: () -> Unit
) {
    val repository = remember { AuthRepositoryFirebase() }
    val factory = remember { RegistroViewModelFactory(RegistrarUseCase(repository)) }
    val viewModel: RegistroViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val actions = remember(viewModel) {
        RegistroActions(
            onNombreChange = { nuevoValor -> viewModel.onNombreChange(nuevoValor) },
            onApellidoChange = { nuevoValor -> viewModel.onApellidoChange(nuevoValor) },
            onEmailChange = { nuevoValor -> viewModel.onEmailChange(nuevoValor) },
            onPasswordChange = { nuevoValor -> viewModel.onPasswordChange(nuevoValor) },
            onConfirmarPasswordChange = { nuevoValor -> viewModel.onConfirmarPasswordChange(nuevoValor) },
            onRegistrar = { viewModel.registrar(onRegistrado) },
            onIrALogin = onIrALogin
        )
    }

    RegistroScreen(
        uiState = uiState,
        actions = actions
    )
}

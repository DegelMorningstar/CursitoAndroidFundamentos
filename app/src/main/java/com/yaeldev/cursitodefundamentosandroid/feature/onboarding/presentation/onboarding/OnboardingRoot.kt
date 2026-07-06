package com.yaeldev.cursitodefundamentosandroid.feature.onboarding.presentation.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yaeldev.cursitodefundamentosandroid.core.di.appContainer

/**
 * Stateful: obtiene el [OnboardingViewModel], colecta el estado y cablea las acciones.
 * [onTerminado] (parametro) lo dispara la navegacion al completar u omitir.
 */
@Composable
fun OnboardingRoot(
    onTerminado: () -> Unit
) {
    val viewModel: OnboardingViewModel = viewModel(factory = appContainer().onboarding.factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val actions = remember(viewModel) {
        OnboardingActions(
            onSiguiente = { viewModel.siguiente(onTerminado) },
            onOmitir = { viewModel.omitir(onTerminado) }
        )
    }

    OnboardingScreen(
        uiState = uiState,
        actions = actions
    )
}

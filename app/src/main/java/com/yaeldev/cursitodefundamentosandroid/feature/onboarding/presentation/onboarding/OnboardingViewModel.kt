package com.yaeldev.cursitodefundamentosandroid.feature.onboarding.presentation.onboarding

import androidx.lifecycle.ViewModel
import com.yaeldev.cursitodefundamentosandroid.feature.onboarding.domain.usecases.MarcarOnboardingCompletadoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Lleva el paso actual del onboarding. Al avanzar en la ultima pagina (o al omitir)
 * marca el onboarding como completado y avisa a la navegacion via [onTerminado].
 */
class OnboardingViewModel(
    private val marcarCompletado: MarcarOnboardingCompletadoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun siguiente(onTerminado: () -> Unit) {
        val estado = _uiState.value
        if (estado.esUltima) {
            marcarCompletado()
            onTerminado()
        } else {
            _uiState.update { it.copy(paso = it.paso + 1) }
        }
    }

    fun omitir(onTerminado: () -> Unit) {
        marcarCompletado()
        onTerminado()
    }
}

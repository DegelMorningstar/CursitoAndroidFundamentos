package com.yaeldev.cursitodefundamentosandroid.feature.onboarding.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaeldev.cursitodefundamentosandroid.feature.onboarding.domain.usecases.OnboardingCompletadoUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** A donde ir cuando termina el splash. */
enum class SplashDestino { Onboarding, Login, Home }

/**
 * Mantiene el branding en pantalla un instante y luego decide el destino: si el
 * onboarding no se ha visto -> Onboarding; si hay sesion activa -> Home; si no -> Login.
 * `null` significa "todavia mostrando el splash".
 */
class SplashViewModel(
    private val onboardingCompletado: OnboardingCompletadoUseCase
) : ViewModel() {

    private val _destino = MutableStateFlow<SplashDestino?>(null)
    val destino: StateFlow<SplashDestino?> = _destino.asStateFlow()

    fun decidir(haySesion: Boolean) {
        if (_destino.value != null) return   // ya decidido (evita re-disparar en recomposicion)
        viewModelScope.launch {
            delay(DURACION_SPLASH_MS)
            _destino.update {
                when {
                    !onboardingCompletado() -> SplashDestino.Onboarding
                    haySesion -> SplashDestino.Home
                    else -> SplashDestino.Login
                }
            }
        }
    }

    private companion object {
        const val DURACION_SPLASH_MS = 1900L
    }
}

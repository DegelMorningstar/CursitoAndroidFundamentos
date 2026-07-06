package com.yaeldev.cursitodefundamentosandroid.feature.onboarding.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.feature.onboarding.domain.repositories.OnboardingPreferences

/** Marca el onboarding como completado (al terminarlo u omitirlo). */
class MarcarOnboardingCompletadoUseCase(
    private val preferencias: OnboardingPreferences
) {
    operator fun invoke() = preferencias.marcarCompletado()
}

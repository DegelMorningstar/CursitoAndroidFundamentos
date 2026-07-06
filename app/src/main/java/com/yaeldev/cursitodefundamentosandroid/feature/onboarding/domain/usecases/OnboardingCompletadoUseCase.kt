package com.yaeldev.cursitodefundamentosandroid.feature.onboarding.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.feature.onboarding.domain.repositories.OnboardingPreferences

/** Indica si el onboarding ya fue visto en este dispositivo. */
class OnboardingCompletadoUseCase(
    private val preferencias: OnboardingPreferences
) {
    operator fun invoke(): Boolean = preferencias.completado()
}

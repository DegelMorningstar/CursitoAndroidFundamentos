package com.yaeldev.cursitodefundamentosandroid.feature.onboarding.domain.repositories

/**
 * Preferencia local (persistida) de si el usuario ya completo/omitio el onboarding
 * de bienvenida. Es una preferencia de dispositivo, por eso NO es una operacion de
 * red (sin `suspend`/`Result`): lee/escribe SharedPreferences de forma sincrona.
 */
interface OnboardingPreferences {
    /** true si el onboarding ya se vio (o se omitio) en este dispositivo. */
    fun completado(): Boolean

    /** Marca el onboarding como completado para no volver a mostrarlo. */
    fun marcarCompletado()
}

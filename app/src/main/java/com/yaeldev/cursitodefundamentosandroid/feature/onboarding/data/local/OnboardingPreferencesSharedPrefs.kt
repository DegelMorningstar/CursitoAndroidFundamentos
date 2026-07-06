package com.yaeldev.cursitodefundamentosandroid.feature.onboarding.data.local

import android.content.Context
import androidx.core.content.edit
import com.yaeldev.cursitodefundamentosandroid.feature.onboarding.domain.repositories.OnboardingPreferences

/**
 * Implementacion de [OnboardingPreferences] sobre `SharedPreferences`. Guarda un
 * unico flag local (por dispositivo) para no repetir el onboarding en cada arranque.
 */
class OnboardingPreferencesSharedPrefs(
    context: Context
) : OnboardingPreferences {

    private val prefs = context.getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE)

    override fun completado(): Boolean = prefs.getBoolean(CLAVE_COMPLETADO, false)

    override fun marcarCompletado() {
        prefs.edit { putBoolean(CLAVE_COMPLETADO, true) }
    }

    private companion object {
        const val ARCHIVO = "onboarding_prefs"
        const val CLAVE_COMPLETADO = "completado"
    }
}

package com.yaeldev.cursitodefundamentosandroid.core.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.core.domain.repositories.UserPreferences

class CambiarTemaUseCase(
    private val preferences: UserPreferences
) {
    operator fun invoke(isDarkMode: Boolean) {
        preferences.guardarTema(isDarkMode)
    }
}
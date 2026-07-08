package com.yaeldev.cursitodefundamentosandroid.core.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.core.domain.repositories.UserPreferences

class ObtenerTemaUseCase(
    private val preferences: UserPreferences
) {
    operator fun invoke() = preferences.leerTema()
}
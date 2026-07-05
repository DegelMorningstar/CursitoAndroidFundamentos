package com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.repositories.AuthRepository

/** Cierra la sesion actual. */
class CerrarSesionUseCase(
    private val repository: AuthRepository
) {
    operator fun invoke() = repository.cerrarSesion()
}

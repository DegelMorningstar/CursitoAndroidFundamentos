package com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.models.Usuario
import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.repositories.AuthRepository
import com.yaeldev.cursitodefundamentosandroid.core.network.Result

/** Inicia sesion de un usuario existente con correo y contraseña. */
class IniciarSesionUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Usuario> =
        repository.iniciarSesion(email, password)
}

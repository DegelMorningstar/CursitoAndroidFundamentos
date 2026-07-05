package com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.models.Usuario
import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.repositories.AuthRepository
import com.yaeldev.cursitodefundamentosandroid.core.network.Result

/** Da de alta una cuenta nueva con nombre, correo y contraseña. */
class RegistrarUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        nombre: String,
        apellido: String,
        email: String,
        password: String
    ): Result<Usuario> = repository.registrar(nombre, apellido, email, password)
}

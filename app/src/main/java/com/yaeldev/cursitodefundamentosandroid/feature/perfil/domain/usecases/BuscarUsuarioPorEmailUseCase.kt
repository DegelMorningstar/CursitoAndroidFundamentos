package com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.core.network.Result
import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.models.Usuario
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.repositories.PerfilRepository

/** Busca un usuario de la app por su correo (directorio publico). null si no existe. */
class BuscarUsuarioPorEmailUseCase(
    private val repository: PerfilRepository
) {
    suspend operator fun invoke(email: String): Result<Usuario?> = repository.buscarPorEmail(email)
}
package com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.core.network.Result
import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.models.Usuario
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.data.remote.firebase.PerfilRepositoryFirebase

/** Busca un usuario de la app por su correo (directorio publico). null si no existe. */
class BuscarUsuarioPorEmailUseCase(
    private val repository: PerfilRepositoryFirebase
) {
    suspend operator fun invoke(email: String): Result<Usuario?> = repository.buscarPorEmail(email)
}
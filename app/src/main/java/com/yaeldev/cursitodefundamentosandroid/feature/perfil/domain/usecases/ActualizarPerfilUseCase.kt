package com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.core.network.Result
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.models.EstadoUsuario
import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.models.Usuario
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.data.remote.firebase.PerfilRepositoryFirebase

/** Actualiza nombre, foto y estado del usuario actual. */
class ActualizarPerfilUseCase(
    private val repository: PerfilRepositoryFirebase
) {
    suspend operator fun invoke(
        nombre: String,
        foto: String,
        estado: EstadoUsuario
    ): Result<Usuario> = repository.actualizarPerfil(nombre, foto, estado)
}
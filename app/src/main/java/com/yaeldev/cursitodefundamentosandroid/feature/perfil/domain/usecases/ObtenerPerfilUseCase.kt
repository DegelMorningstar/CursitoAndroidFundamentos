package com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.core.network.Result
import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.models.Usuario
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.data.remote.firebase.PerfilRepositoryFirebase

/** Obtiene el perfil completo (nombre, foto, estado) del usuario actual. */
class ObtenerPerfilUseCase(
    private val repository: PerfilRepositoryFirebase
) {
    suspend operator fun invoke(): Result<Usuario?> = repository.obtenerPerfil()
}
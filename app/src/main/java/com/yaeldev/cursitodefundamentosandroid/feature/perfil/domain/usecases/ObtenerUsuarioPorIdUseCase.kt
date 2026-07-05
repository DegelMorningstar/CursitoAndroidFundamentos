package com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.models.Usuario
import com.yaeldev.cursitodefundamentosandroid.core.network.Result
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.data.remote.firebase.PerfilRepositoryFirebase

/** Obtiene el perfil (nombre, foto, estado) de un usuario por su uid. */
class ObtenerUsuarioPorIdUseCase(
    private val repository: PerfilRepositoryFirebase
) {
    suspend operator fun invoke(uid: String): Result<Usuario?> = repository.obtenerUsuarioPorId(uid)
}

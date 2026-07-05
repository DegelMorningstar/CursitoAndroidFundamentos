package com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.models.Usuario
import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.repositories.AuthRepository
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.data.remote.firebase.PerfilRepositoryFirebase

/** Devuelve el usuario con sesion activa, o null si no hay sesion. */
class ObtenerUsuarioActualUseCase(
    private val repository: PerfilRepositoryFirebase
) {
    operator fun invoke(): Usuario? = repository.usuarioActual()
}

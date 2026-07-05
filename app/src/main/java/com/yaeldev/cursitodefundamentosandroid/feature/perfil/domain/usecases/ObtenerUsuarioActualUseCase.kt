package com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.models.Usuario
import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.repositories.AuthRepository
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.repositories.PerfilRepository

/** Devuelve el usuario con sesion activa, o null si no hay sesion. */
class ObtenerUsuarioActualUseCase(
    private val repository: PerfilRepository
) {
    operator fun invoke(): Usuario? = repository.usuarioActual()
}

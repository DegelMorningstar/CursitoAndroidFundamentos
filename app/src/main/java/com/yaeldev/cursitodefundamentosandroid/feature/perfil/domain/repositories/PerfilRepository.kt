package com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.repositories

import com.yaeldev.cursitodefundamentosandroid.core.network.Result
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.models.EstadoUsuario
import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.models.Usuario

interface PerfilRepository {
    /** Usuario con sesion activa, o null si nadie ha iniciado sesion. */
    fun usuarioActual(): Usuario?
    /**
     * Busca en el directorio de usuarios por correo.
     * Devuelve Success(null) si nadie usa ese correo. Sirve para saber si un contacto
     * es usuario de la app y poder chatear.
     */
    suspend fun buscarPorEmail(email: String): Result<Usuario?>
    /** Perfil completo del usuario actual (incluye foto/estado). */
    suspend fun obtenerPerfil(): Result<Usuario?>
    /** Perfil de cualquier usuario por su uid (para ver el de un contacto/chat). */
    suspend fun obtenerUsuarioPorId(uid: String): Result<Usuario?>
    /** Actualiza nombre, foto (base64) y estado del usuario actual. */
    suspend fun actualizarPerfil(
        nombre: String,
        foto: String,
        estado: EstadoUsuario
    ): Result<Usuario>
}
package com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.repositories

import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.models.Usuario
import com.yaeldev.cursitodefundamentosandroid.core.network.Result

interface AuthRepository {

    /** Crea una cuenta nueva con correo y contraseña; devuelve el usuario creado. */
    suspend fun registrar(
        nombre: String,
        apellido: String,
        email: String,
        password: String
    ): Result<Usuario>

    /** Inicia sesion con correo y contraseña; devuelve el usuario autenticado. */
    suspend fun iniciarSesion(email: String, password: String): Result<Usuario>

    /** Cierra la sesion actual (si la hay). */
    fun cerrarSesion()
}

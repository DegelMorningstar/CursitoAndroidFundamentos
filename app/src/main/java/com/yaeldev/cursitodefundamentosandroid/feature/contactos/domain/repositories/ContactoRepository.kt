package com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.repositories

import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.models.Contacto
import com.yaeldev.cursitodefundamentosandroid.core.network.Result

interface ContactoRepository {

    /** GET /contacts — todos, ordenados alfabeticamente por nombre completo. */
    suspend fun obtenerTodos(): Result<List<Contacto>>

    /** GET /contacts/{id} — Success(null) si no existe. */
    suspend fun obtenerPorId(id: String): Result<Contacto?>

    /** POST /contacts — el data source asigna el id; devuelve el contacto creado. */
    suspend fun agregar(contacto: Contacto): Result<Contacto>

    /** PUT /contacts/{id} — devuelve el contacto actualizado. */
    suspend fun actualizar(contacto: Contacto): Result<Contacto>

    /** DELETE /contacts/{id}. */
    suspend fun eliminar(id: String): Result<Unit>

    /** Atajo para alternar el favorito; devuelve el contacto resultante. */
    suspend fun alternarFavorito(id: String): Result<Contacto>
}

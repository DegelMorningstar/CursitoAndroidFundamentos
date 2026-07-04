package com.yaeldev.cursitodefundamentosandroid.domain.repositories

import com.yaeldev.cursitodefundamentosandroid.domain.models.Contacto
import com.yaeldev.cursitodefundamentosandroid.util.Result

interface ContactoRepository {

    /** GET /contacts — todos, ordenados alfabeticamente por nombre completo. */
    suspend fun obtenerTodos(): Result<List<Contacto>>

    /** GET /contacts/{id} — null si no existe. */
    suspend fun obtenerPorId(id: Int): Contacto?

    /** POST /contacts — el data source asigna el id; devuelve el contacto creado. */
    suspend fun agregar(contacto: Contacto): Contacto

    /** PUT /contacts/{id} — devuelve el contacto actualizado. */
    suspend fun actualizar(contacto: Contacto): Contacto

    /** DELETE /contacts/{id}. */
    suspend fun eliminar(id: Int)

    /** Atajo para alternar el favorito; devuelve el contacto resultante. */
    suspend fun alternarFavorito(id: Int): Contacto
}
package com.yaeldev.cursitodefundamentosandroid.data

import kotlinx.serialization.Serializable

@Serializable
data class Contacto(
    val id: Int = 0,            // 0 = aun no asignado; el data source pone el id real
    val first: String,          // requerido
    val last: String = "",      // opcional
    val phone: String,          // requerido
    val email: String = "",     // opcional
    val company: String = "",   // opcional
    val favorite: Boolean = false
) {
    /** Nombre completo para mostrar (ordenamiento y filtros). */
    val nombreCompleto: String
        get() = listOf(first, last).filter { it.isNotBlank() }.joinToString(" ")

    /** Iniciales para el avatar: 1ra de first + 1ra de last, en mayuscula. */
    val iniciales: String
        get() = buildString {
            first.firstOrNull()?.let { append(it.uppercaseChar()) }
            last.firstOrNull()?.let { append(it.uppercaseChar()) }
        }.ifEmpty { "?" }
}

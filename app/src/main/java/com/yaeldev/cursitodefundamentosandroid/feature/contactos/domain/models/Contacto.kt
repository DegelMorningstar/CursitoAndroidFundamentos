package com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.models

data class Contacto(
    val id: String = "",        // ID del documento en Firestore ("" = aun no asignado)
    val first: String,          // requerido
    val last: String = "",      // opcional
    val phone: String,          // requerido
    val email: String = "",     // opcional
    val company: String = "",   // opcional
    val favorite: Boolean = false,
    val owner: String = ""      // uid del usuario dueño; lo sella el repositorio
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

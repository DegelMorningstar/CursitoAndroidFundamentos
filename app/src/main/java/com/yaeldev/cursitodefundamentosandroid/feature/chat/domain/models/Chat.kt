package com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.models

/**
 * Una conversacion 1:1. [participantes] son los dos uids; [nombres] guarda el
 * nombre de cada uno (denormalizado) para mostrar el chat sin lecturas extra.
 * [ultimoMensaje]/[ultimoTimestamp] alimentan la vista previa en la lista.
 */
data class Chat(
    val id: String = "",
    val participantes: List<String> = emptyList(),
    val nombres: Map<String, String> = emptyMap(),
    val ultimoMensaje: String = "",
    val ultimoTimestamp: Long = 0L
) {
    /** uid del otro participante (el que no soy yo). */
    fun otroUid(miUid: String): String = participantes.firstOrNull { it != miUid }.orEmpty()

    /** Nombre del otro participante para el titulo/lista. */
    fun otroNombre(miUid: String): String =
        nombres[otroUid(miUid)]?.takeIf { it.isNotBlank() } ?: "Usuario"

    /** Inicial del otro participante para el avatar. */
    fun inicial(miUid: String): String =
        otroNombre(miUid).firstOrNull()?.uppercaseChar()?.toString() ?: "?"
}

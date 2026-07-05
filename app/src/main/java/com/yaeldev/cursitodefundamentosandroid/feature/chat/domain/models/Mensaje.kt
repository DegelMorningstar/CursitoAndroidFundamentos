package com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.models

/**
 * Un mensaje dentro de un chat. [autor] es el uid de quien lo envio; [timestamp]
 * en millis (0 = aun sin resolver el serverTimestamp). [leido] lo marca el receptor
 * al abrir la conversacion (para el estado entregado/leido).
 */
data class Mensaje(
    val id: String = "",
    val autor: String = "",
    val texto: String = "",
    val timestamp: Long = 0L,
    val leido: Boolean = false
)

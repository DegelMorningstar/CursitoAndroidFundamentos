package com.yaeldev.cursitodefundamentosandroid.core.domain.repositories

interface NotificadorRepository {
    suspend fun enviar(destinatarioUid: String, titulo: String, cuerpo: String, grupo: String? = null)
}
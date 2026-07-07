package com.yaeldev.cursitodefundamentosandroid.feature.chat.data.remote.push

import com.google.firebase.auth.FirebaseAuth
import com.yaeldev.cursitodefundamentosandroid.core.notifications.push.NotificadorPush

/**
 * Notificador **concreto de chat**: sabe cómo armar el push de "nuevo mensaje"
 * (destinatario, título, agrupación por conversación) y delega el envío en el
 * [NotificadorPush] genérico.
 */
class NotificadorMensajesChat(
    private val notificadorPush: NotificadorPush,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    suspend fun notificarNuevoMensaje(chatId: String, texto: String) {
        val miUid = auth.currentUser?.uid ?: return
        // chatId es determinista (uids ordenados unidos por "_"): el otro es el destinatario.
        val destinatarioUid = chatId.split("_").firstOrNull { it != miUid } ?: return
        val titulo = auth.currentUser?.displayName?.takeIf { it.isNotBlank() } ?: "Nuevo mensaje"
        // grupo = chatId -> los mensajes de la misma conversación se colapsan en una notificación.
        notificadorPush.enviar(
            destinatarioUid = destinatarioUid,
            titulo = titulo,
            cuerpo = texto,
            grupo = chatId
        )
    }
}

package com.yaeldev.cursitodefundamentosandroid.core.notifications

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.yaeldev.cursitodefundamentosandroid.core.util.Catalogo

/**
 * Recibe los push de FCM. Con payload de tipo `notification`, el sistema muestra la
 * notificación automáticamente cuando la app está en segundo plano; [onMessageReceived]
 * solo se dispara en **primer plano**, así que ahí la mostramos a mano.
 */
class CursitoMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        // El token puede rotar; lo re-guardamos si hay sesión.
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        Firebase.firestore.collection(Catalogo.USUARIOS).document(uid)
            .set(mapOf(Catalogo.CAMPO_FCM_TOKEN to token), SetOptions.merge())
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val notificacion = message.notification ?: return
        mostrarNotificacionMensaje(
            context = this,
            titulo = notificacion.title ?: "Nuevo mensaje",
            cuerpo = notificacion.body.orEmpty(),
            grupo = message.data["grupo"]   // agrupa por conversación (chatId)
        )
    }
}

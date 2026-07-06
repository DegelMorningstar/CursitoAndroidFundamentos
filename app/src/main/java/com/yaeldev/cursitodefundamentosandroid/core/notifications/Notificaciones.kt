package com.yaeldev.cursitodefundamentosandroid.core.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.google.firebase.messaging.FirebaseMessaging
import com.yaeldev.cursitodefundamentosandroid.core.util.Catalogo

const val CANAL_MENSAJES = "mensajes"

/** Crea el canal de notificaciones de mensajes (necesario en Android 8+). */
fun crearCanalMensajes(context: Context) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
    val canal = NotificationChannel(
        CANAL_MENSAJES,
        "Mensajes",
        NotificationManager.IMPORTANCE_HIGH
    ).apply { description = "Notificaciones de chats" }
    context.getSystemService(NotificationManager::class.java)
        .createNotificationChannel(canal)
}

/**
 * Muestra una notificación local (usada cuando llega un push con la app en primer plano).
 * Si [grupo] viene, se usa un ID estable derivado de él: los mensajes del mismo grupo
 * (p. ej. la misma conversación) **actualizan la misma notificación** en vez de apilarse.
 */
fun mostrarNotificacionMensaje(context: Context, titulo: String, cuerpo: String, grupo: String? = null) {
    val notificacion = NotificationCompat.Builder(context, CANAL_MENSAJES)
        .setSmallIcon(android.R.drawable.stat_notify_chat)
        .setContentTitle(titulo)
        .setContentText(cuerpo)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .build()
    val id = grupo?.hashCode() ?: System.currentTimeMillis().toInt()
    try {
        // En Android 13+ requiere permiso POST_NOTIFICATIONS; si no está, no muestra nada.
        NotificationManagerCompat.from(context).notify(id, notificacion)
    } catch (e: SecurityException) {
        // Sin permiso: lo ignoramos.
    }
}

/**
 * Registra el token FCM del dispositivo en `usuarios/{uid}.fcmToken`, para que el
 * Worker sepa a dónde enviar los push. No hace nada si no hay sesión.
 */
fun registrarTokenFcm() {
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
    FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
        Firebase.firestore.collection(Catalogo.USUARIOS).document(uid)
            .set(mapOf(Catalogo.CAMPO_FCM_TOKEN to token), SetOptions.merge())
    }
}

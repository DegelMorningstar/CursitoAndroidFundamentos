package com.yaeldev.cursitodefundamentosandroid.core.notifications.push

import com.google.firebase.auth.FirebaseAuth
import com.yaeldev.cursitodefundamentosandroid.core.util.Catalogo.WORKER_PUSH_URL
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

interface NotificadorPush {
    suspend fun enviar(destinatarioUid: String, titulo: String, cuerpo: String, grupo: String? = null)
}

class NotificadorPushWorker(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val pushApi: PushApi = PushApiFactory.create()
) : NotificadorPush {

    override suspend fun enviar(
        destinatarioUid: String,
        titulo: String,
        cuerpo: String,
        grupo: String?
    ) {
        if (WORKER_PUSH_URL.isBlank()) return
        try {
            val idToken = auth.currentUser?.getIdToken(false)?.await()?.token ?: return
            pushApi.notificar(PushRequest(idToken, destinatarioUid, titulo, cuerpo, grupo))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            // Best-effort: ignoramos fallos de notificación.
        }
    }
}

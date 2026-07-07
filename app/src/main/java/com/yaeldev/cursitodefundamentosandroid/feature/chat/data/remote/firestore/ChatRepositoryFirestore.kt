package com.yaeldev.cursitodefundamentosandroid.feature.chat.data.remote.firestore

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.models.Chat
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.models.Mensaje
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.repositories.ChatRepository
import com.yaeldev.cursitodefundamentosandroid.core.network.Result
import com.yaeldev.cursitodefundamentosandroid.core.network.ejecutar
import com.yaeldev.cursitodefundamentosandroid.core.network.toMessage
import com.yaeldev.cursitodefundamentosandroid.core.util.Catalogo.CHATS
import com.yaeldev.cursitodefundamentosandroid.core.util.Catalogo.MENSAJES
import com.yaeldev.cursitodefundamentosandroid.core.notifications.push.NotificadorPushWorker
import com.yaeldev.cursitodefundamentosandroid.feature.chat.data.remote.dto.MensajeDocument
import com.yaeldev.cursitodefundamentosandroid.feature.chat.data.remote.mappers.toChat
import com.yaeldev.cursitodefundamentosandroid.feature.chat.data.remote.mappers.toMensaje
import com.yaeldev.cursitodefundamentosandroid.feature.chat.data.remote.push.NotificadorMensajesChat
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ChatRepositoryFirestore(
    private val db: FirebaseFirestore = Firebase.firestore,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val notificadorChat: NotificadorMensajesChat = NotificadorMensajesChat(NotificadorPushWorker())
) : ChatRepository {

    private val chats get() = db.collection(CHATS)
    private fun mensajesDe(chatId: String) = chats.document(chatId).collection(MENSAJES)

    override fun uidActual(): String? = auth.currentUser?.uid

    override fun observarChats(): Flow<Result<List<Chat>>> = callbackFlow {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            trySend(Result.Error("No hay sesion activa"))
            close()
            return@callbackFlow
        }
        val registro = chats
            .whereArrayContains("participantes", uid)
            .orderBy("ultimoTimestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, e ->
                if (e != null) {
                    trySend(Result.Error(e.toMessage()))
                    return@addSnapshotListener
                }
                val lista = snap?.documents?.mapNotNull { it.toChat() }.orEmpty()
                trySend(Result.Success(lista))
            }
        awaitClose { registro.remove() }
    }

    override fun observarMensajes(chatId: String): Flow<Result<List<Mensaje>>> = callbackFlow {
        val registro = mensajesDe(chatId)
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snap, e ->
                if (e != null) {
                    trySend(Result.Error(e.toMessage()))
                    return@addSnapshotListener
                }
                val lista = snap?.documents?.mapNotNull { it.toMensaje() }.orEmpty()
                trySend(Result.Success(lista))
            }
        awaitClose { registro.remove() }
    }

    override suspend fun enviarMensaje(chatId: String, texto: String): Result<Unit> = ejecutar {
        val mensaje = MensajeDocument(autor = uid(), texto = texto)
        mensajesDe(chatId).add(mensaje).await()
        // Actualizamos la vista previa del chat (y su orden en la lista).
        chats.document(chatId).set(
            mapOf(
                "ultimoMensaje" to texto,
                "ultimoTimestamp" to FieldValue.serverTimestamp()
            ),
            SetOptions.merge()
        ).await()
        // Dispara el push (best-effort): no debe afectar el resultado del envío.
        notificadorChat.notificarNuevoMensaje(chatId, texto)
        Unit
    }

    override suspend fun abrirOCrearChat(otroUid: String, otroNombre: String): Result<String> =
        ejecutar {
            val miUid = uid()
            if (otroUid == miUid) throw IllegalArgumentException("No puedes chatear contigo mismo")
            val miNombre = auth.currentUser?.displayName.orEmpty()
            val participantes = listOf(miUid, otroUid).sorted()
            val chatId = participantes.joinToString("_")
            // Merge idempotente: no pisa ultimoMensaje/ultimoTimestamp si ya existe.
            chats.document(chatId).set(
                mapOf(
                    "participantes" to participantes,
                    "nombres" to mapOf(miUid to miNombre, otroUid to otroNombre)
                ),
                SetOptions.merge()
            ).await()
            chatId
        }

    override suspend fun marcarComoLeidos(chatId: String): Result<Unit> = ejecutar {
        val miUid = uid()
        val pendientes = mensajesDe(chatId).whereEqualTo("leido", false).get().await()
        val recibidos = pendientes.documents.filter { it.getString("autor") != miUid }
        if (recibidos.isNotEmpty()) {
            val batch = db.batch()
            recibidos.forEach { batch.update(it.reference, "leido", true) }
            batch.commit().await()
        }
        Unit
    }

    private fun uid(): String =
        auth.currentUser?.uid ?: throw IllegalStateException("No hay sesion activa")
}

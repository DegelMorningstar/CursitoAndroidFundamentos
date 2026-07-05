package com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.repositories

import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.models.Chat
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.models.Mensaje
import com.yaeldev.cursitodefundamentosandroid.core.network.Result
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de chat. A diferencia del resto de repositorios (one-shot `suspend`),
 * las lecturas de chat son **streaming**: exponen `Flow` respaldado por los
 * snapshot listeners de Firestore, para recibir mensajes en tiempo real.
 */
interface ChatRepository {

    /** Mis conversaciones en tiempo real, ordenadas por actividad reciente. */
    fun observarChats(): Flow<Result<List<Chat>>>

    /** Mensajes de un chat en tiempo real, en orden cronologico. */
    fun observarMensajes(chatId: String): Flow<Result<List<Mensaje>>>

    /** Envia un mensaje y actualiza la vista previa del chat. */
    suspend fun enviarMensaje(chatId: String, texto: String): Result<Unit>

    /** Abre (o crea si no existe) el chat 1:1 con [otroUid]; devuelve el chatId. */
    suspend fun abrirOCrearChat(otroUid: String, otroNombre: String): Result<String>

    /** Marca como leidos los mensajes recibidos (no propios) del chat. */
    suspend fun marcarComoLeidos(chatId: String): Result<Unit>

    /** uid del usuario con sesion activa (para distinguir mis mensajes), o null. */
    fun uidActual(): String?
}

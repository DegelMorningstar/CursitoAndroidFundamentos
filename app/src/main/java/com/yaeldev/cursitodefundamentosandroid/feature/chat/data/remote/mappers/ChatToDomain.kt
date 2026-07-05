package com.yaeldev.cursitodefundamentosandroid.feature.chat.data.remote.mappers

import com.google.firebase.firestore.DocumentSnapshot
import com.yaeldev.cursitodefundamentosandroid.feature.chat.data.remote.dto.ChatDocument
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.models.Chat

fun DocumentSnapshot.toChat(): Chat? {
    val doc = toObject(ChatDocument::class.java) ?: return null
    return Chat(
        id = id,
        participantes = doc.participantes,
        nombres = doc.nombres,
        ultimoMensaje = doc.ultimoMensaje,
        ultimoTimestamp = doc.ultimoTimestamp?.toDate()?.time ?: 0L
    )
}
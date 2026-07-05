package com.yaeldev.cursitodefundamentosandroid.feature.chat.data.remote.mappers

import com.google.firebase.firestore.DocumentSnapshot
import com.yaeldev.cursitodefundamentosandroid.feature.chat.data.remote.dto.MensajeDocument
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.models.Mensaje

fun DocumentSnapshot.toMensaje(): Mensaje? {
    val doc = toObject(MensajeDocument::class.java) ?: return null
    return Mensaje(
        id = id,
        autor = doc.autor,
        texto = doc.texto,
        timestamp = doc.timestamp?.toDate()?.time ?: 0L,
        leido = doc.leido
    )
}

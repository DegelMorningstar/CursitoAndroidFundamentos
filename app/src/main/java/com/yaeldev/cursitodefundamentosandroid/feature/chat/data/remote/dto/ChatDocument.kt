package com.yaeldev.cursitodefundamentosandroid.feature.chat.data.remote.dto

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
data class ChatDocument(
    val participantes: List<String> = emptyList(),
    val nombres: Map<String, String> = emptyMap(),
    val ultimoMensaje: String = "",
    @ServerTimestamp val ultimoTimestamp: Timestamp? = null
)




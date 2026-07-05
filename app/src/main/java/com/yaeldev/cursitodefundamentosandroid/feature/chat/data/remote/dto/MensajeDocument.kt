package com.yaeldev.cursitodefundamentosandroid.feature.chat.data.remote.dto

import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.Timestamp

data class MensajeDocument(
    val autor: String = "",
    val texto: String = "",
    @ServerTimestamp val timestamp: Timestamp? = null,
    val leido: Boolean = false
)
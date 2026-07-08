package com.yaeldev.cursitodefundamentosandroid.core.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PushRequest(
    val idToken: String,
    val destinatarioUid: String,
    val titulo: String,
    val cuerpo: String,
    val grupo: String? = null
)
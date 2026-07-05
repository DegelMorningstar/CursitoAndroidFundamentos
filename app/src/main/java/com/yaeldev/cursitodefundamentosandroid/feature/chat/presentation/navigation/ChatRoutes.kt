package com.yaeldev.cursitodefundamentosandroid.feature.chat.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object ListaChats

/**
 * El chat resuelve sus mensajes desde el repositorio a partir del chatId
 * */
@Serializable
data class Conversacion(val chatId: String, val titulo: String, val otroUid: String)
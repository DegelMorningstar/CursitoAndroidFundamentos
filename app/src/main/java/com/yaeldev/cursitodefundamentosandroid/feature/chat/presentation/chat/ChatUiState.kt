package com.yaeldev.cursitodefundamentosandroid.feature.chat.presentation.chat

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.models.Mensaje

data class ChatUiState(
    val titulo: String = "",
    val miUid: String = "",
    val mensajes: List<Mensaje> = emptyList(),
    val texto: String = "",
    val cargando: Boolean = true,
    val error: String? = null
)

data class ChatActions(
    val onTextoChange: (String) -> Unit = {},
    val onEnviar: () -> Unit = {},
    val onBack: () -> Unit = {},
    val onVerPerfil: () -> Unit = {}
)

class ChatPreviewParameterProvider : PreviewParameterProvider<ChatUiState> {
    override val values: Sequence<ChatUiState>
        get() = sequenceOf(
            ChatUiState(
                titulo = "Ruben Estrada",
                miUid = "yo",
                cargando = false,
                mensajes = listOf(
                    Mensaje(id = "1", autor = "ruben", texto = "Hola, ¿todo bien?", timestamp = 1L, leido = true),
                    Mensaje(id = "2", autor = "yo", texto = "Todo bien, ¿y tu?", timestamp = 2L, leido = true),
                    Mensaje(id = "3", autor = "yo", texto = "¿Nos vemos mañana?", timestamp = 3L, leido = false)
                )
            ),
            ChatUiState(titulo = "Ana Lopez", miUid = "yo", cargando = false),
            ChatUiState(titulo = "Cargando", miUid = "yo", cargando = true)
        )
}

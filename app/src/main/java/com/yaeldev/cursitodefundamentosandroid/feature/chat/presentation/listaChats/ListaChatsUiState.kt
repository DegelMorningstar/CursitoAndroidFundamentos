package com.yaeldev.cursitodefundamentosandroid.feature.chat.presentation.listaChats

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.models.Chat

sealed interface ListaChatsUiState {
    data object Loading : ListaChatsUiState
    data object Empty : ListaChatsUiState
    data class Error(val message: String) : ListaChatsUiState
    data class Success(
        val chats: List<Chat>,
        val miUid: String
    ) : ListaChatsUiState
}

data class ListaChatsActions(
    val onAbrirChat: (chatId: String, titulo: String, otroUid: String) -> Unit = { _, _, _ -> },
    val onNavigateToContactos: () -> Unit = {},
    val onNavigateToFavoritos: () -> Unit = {},
    val onNavigateToPerfil: () -> Unit = {}
)

class ListaChatsPreviewParameterProvider : PreviewParameterProvider<ListaChatsUiState> {
    override val values: Sequence<ListaChatsUiState>
        get() = sequenceOf(
            ListaChatsUiState.Loading,
            ListaChatsUiState.Empty,
            ListaChatsUiState.Error("No se pudieron cargar los chats."),
            ListaChatsUiState.Success(
                miUid = "yo",
                chats = listOf(
                    Chat(
                        id = "yo_ruben",
                        participantes = listOf("yo", "ruben"),
                        nombres = mapOf("yo" to "Yael", "ruben" to "Ruben Estrada"),
                        ultimoMensaje = "¿Nos vemos mañana?",
                        ultimoTimestamp = System.currentTimeMillis()
                    ),
                    Chat(
                        id = "ana_yo",
                        participantes = listOf("ana", "yo"),
                        nombres = mapOf("yo" to "Yael", "ana" to "Ana Lopez"),
                        ultimoMensaje = "Gracias!",
                        ultimoTimestamp = System.currentTimeMillis() - 3_600_000
                    )
                )
            )
        )
}

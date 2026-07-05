package com.yaeldev.cursitodefundamentosandroid.feature.chat.presentation.listaChats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yaeldev.cursitodefundamentosandroid.feature.chat.data.remote.firestore.ChatRepositoryFirestore
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.usecases.ObservarChatsUseCase

@Composable
fun ListaChatsRoot(
    onAbrirChat: (chatId: String, titulo: String, otroUid: String) -> Unit,
    onNavigateToContactos: () -> Unit,
    onNavigateToFavoritos: () -> Unit,
    onNavigateToPerfil: () -> Unit
) {
    val repository = remember { ChatRepositoryFirestore() }
    val miUid = remember { repository.uidActual().orEmpty() }
    val factory = remember { ListaChatsViewModelFactory(ObservarChatsUseCase(repository), miUid) }
    val viewModel: ListaChatsViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val actions = remember(viewModel) {
        ListaChatsActions(
            onAbrirChat = onAbrirChat,
            onNavigateToContactos = onNavigateToContactos,
            onNavigateToFavoritos = onNavigateToFavoritos,
            onNavigateToPerfil = onNavigateToPerfil
        )
    }

    ListaChatsScreen(
        uiState = uiState,
        actions = actions
    )
}

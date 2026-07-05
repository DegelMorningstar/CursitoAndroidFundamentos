package com.yaeldev.cursitodefundamentosandroid.feature.chat.presentation.chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yaeldev.cursitodefundamentosandroid.core.di.appContainer

@Composable
fun ChatRoot(
    chatId: String,
    titulo: String,
    onBack: () -> Unit,
    onVerPerfil: () -> Unit
) {
    val viewModel: ChatViewModel = viewModel(
        factory = appContainer().chat.chatFactory(chatId, titulo)
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val actions = remember(viewModel) {
        ChatActions(
            onTextoChange = { viewModel.onTextoChange(it) },
            onEnviar = { viewModel.enviar() },
            onBack = onBack,
            onVerPerfil = onVerPerfil
        )
    }

    ChatScreen(
        uiState = uiState,
        actions = actions
    )
}

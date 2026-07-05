package com.yaeldev.cursitodefundamentosandroid.feature.chat.presentation.chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yaeldev.cursitodefundamentosandroid.feature.chat.data.remote.firestore.ChatRepositoryFirestore
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.usecases.EnviarMensajeUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.usecases.MarcarLeidosUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.usecases.ObservarMensajesUseCase

@Composable
fun ChatRoot(
    chatId: String,
    titulo: String,
    onBack: () -> Unit,
    onVerPerfil: () -> Unit
) {
    val repository = remember { ChatRepositoryFirestore() }
    val miUid = remember { repository.uidActual().orEmpty() }
    val factory = remember {
        ChatViewModelFactory(
            chatId = chatId,
            titulo = titulo,
            miUid = miUid,
            observarMensajes = ObservarMensajesUseCase(repository),
            enviarMensaje = EnviarMensajeUseCase(repository),
            marcarLeidos = MarcarLeidosUseCase(repository)
        )
    }
    val viewModel: ChatViewModel = viewModel(factory = factory)
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

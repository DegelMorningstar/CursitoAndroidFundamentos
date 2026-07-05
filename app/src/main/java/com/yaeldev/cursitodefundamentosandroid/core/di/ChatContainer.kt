package com.yaeldev.cursitodefundamentosandroid.core.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.repositories.ChatRepository
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.usecases.EnviarMensajeUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.usecases.MarcarLeidosUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.usecases.ObservarChatsUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.usecases.ObservarMensajesUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.chat.presentation.chat.ChatViewModel
import com.yaeldev.cursitodefundamentosandroid.feature.chat.presentation.listaChats.ListaChatsViewModel

/**
 * DI del feature chat. `miUid` sale del propio repositorio (sesion activa), no de
 * la ruta. ChatViewModel si recibe args de ruta (chatId/titulo), por eso se expone
 * un factory-builder aparte de la factory compartida.
 */
class ChatContainer(
    private val chatRepository: ChatRepository
) {
    val factory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            ListaChatsViewModel(
                ObservarChatsUseCase(chatRepository),
                chatRepository.uidActual().orEmpty()
            )
        }
    }

    /** Factory para la conversacion: los args de ruta llegan por parametro. */
    fun chatFactory(chatId: String, titulo: String): ViewModelProvider.Factory = viewModelFactory {
        initializer {
            ChatViewModel(
                chatId = chatId,
                titulo = titulo,
                miUid = chatRepository.uidActual().orEmpty(),
                observarMensajes = ObservarMensajesUseCase(chatRepository),
                enviarMensaje = EnviarMensajeUseCase(chatRepository),
                marcarLeidos = MarcarLeidosUseCase(chatRepository)
            )
        }
    }
}

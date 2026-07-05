package com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.models.Mensaje
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.repositories.ChatRepository
import com.yaeldev.cursitodefundamentosandroid.core.network.Result
import kotlinx.coroutines.flow.Flow

/** Observa los mensajes de un chat en tiempo real. */
class ObservarMensajesUseCase(
    private val repository: ChatRepository
) {
    operator fun invoke(chatId: String): Flow<Result<List<Mensaje>>> =
        repository.observarMensajes(chatId)
}

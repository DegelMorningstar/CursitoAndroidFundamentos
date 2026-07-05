package com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.repositories.ChatRepository
import com.yaeldev.cursitodefundamentosandroid.core.network.Result

/** Envia un mensaje de texto a un chat. */
class EnviarMensajeUseCase(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(chatId: String, texto: String): Result<Unit> =
        repository.enviarMensaje(chatId, texto)
}

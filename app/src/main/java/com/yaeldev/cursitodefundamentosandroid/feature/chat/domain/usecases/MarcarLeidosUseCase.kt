package com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.repositories.ChatRepository
import com.yaeldev.cursitodefundamentosandroid.core.network.Result

/** Marca como leidos los mensajes recibidos de un chat. */
class MarcarLeidosUseCase(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(chatId: String): Result<Unit> = repository.marcarComoLeidos(chatId)
}

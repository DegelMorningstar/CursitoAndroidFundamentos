package com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.repositories.ChatRepository
import com.yaeldev.cursitodefundamentosandroid.core.network.Result

/** Abre (o crea si no existe) el chat 1:1 con otro usuario; devuelve el chatId. */
class AbrirOCrearChatUseCase(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(otroUid: String, otroNombre: String): Result<String> =
        repository.abrirOCrearChat(otroUid, otroNombre)
}

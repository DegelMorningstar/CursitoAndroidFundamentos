package com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.usecases

import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.models.Chat
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.repositories.ChatRepository
import com.yaeldev.cursitodefundamentosandroid.core.network.Result
import kotlinx.coroutines.flow.Flow

/** Observa mis conversaciones en tiempo real. */
class ObservarChatsUseCase(
    private val repository: ChatRepository
) {
    operator fun invoke(): Flow<Result<List<Chat>>> = repository.observarChats()
}

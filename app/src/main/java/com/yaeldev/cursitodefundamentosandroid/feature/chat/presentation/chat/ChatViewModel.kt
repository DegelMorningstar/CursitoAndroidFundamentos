package com.yaeldev.cursitodefundamentosandroid.feature.chat.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.usecases.EnviarMensajeUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.usecases.MarcarLeidosUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.usecases.ObservarMensajesUseCase
import com.yaeldev.cursitodefundamentosandroid.core.network.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatId: String,
    titulo: String,
    miUid: String,
    observarMensajes: ObservarMensajesUseCase,
    private val enviarMensaje: EnviarMensajeUseCase,
    private val marcarLeidos: MarcarLeidosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState(titulo = titulo, miUid = miUid))
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observarMensajes(chatId).collect { resultado ->
                when (resultado) {
                    is Result.Success -> {
                        _uiState.update { it.copy(mensajes = resultado.data, cargando = false, error = null) }
                        // Al recibir mensajes, marcamos como leidos los del otro.
                        marcarLeidos(chatId)
                    }
                    is Result.Error ->
                        _uiState.update { it.copy(cargando = false, error = resultado.message) }
                }
            }
        }
    }

    fun onTextoChange(nuevo: String) {
        _uiState.update { it.copy(texto = nuevo) }
    }

    fun enviar() {
        val texto = _uiState.value.texto.trim()
        if (texto.isBlank()) return
        _uiState.update { it.copy(texto = "") }   // limpiamos el input de inmediato
        viewModelScope.launch {
            when (val resultado = enviarMensaje(chatId, texto)) {
                is Result.Success -> Unit
                is Result.Error -> _uiState.update { it.copy(error = resultado.message) }
            }
        }
    }
}

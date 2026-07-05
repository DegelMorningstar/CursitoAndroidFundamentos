package com.yaeldev.cursitodefundamentosandroid.feature.chat.presentation.listaChats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.usecases.ObservarChatsUseCase
import com.yaeldev.cursitodefundamentosandroid.core.network.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListaChatsViewModel(
    observarChats: ObservarChatsUseCase,
    private val miUid: String
) : ViewModel() {

    private val _uiState = MutableStateFlow<ListaChatsUiState>(ListaChatsUiState.Loading)
    val uiState: StateFlow<ListaChatsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observarChats().collect { resultado ->
                _uiState.update {
                    when (resultado) {
                        is Result.Success ->
                            if (resultado.data.isEmpty()) ListaChatsUiState.Empty
                            else ListaChatsUiState.Success(resultado.data, miUid)
                        is Result.Error -> ListaChatsUiState.Error(resultado.message)
                    }
                }
            }
        }
    }
}

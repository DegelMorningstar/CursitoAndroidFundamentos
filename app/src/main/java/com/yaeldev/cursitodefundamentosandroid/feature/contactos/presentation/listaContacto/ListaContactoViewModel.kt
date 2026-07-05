package com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.listaContacto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases.AlternarFavoritoUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases.ObtenerContactosUseCase
import com.yaeldev.cursitodefundamentosandroid.core.network.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListaContactoViewModel(
    private val obtenerContactos: ObtenerContactosUseCase,
    private val alternarFavoritoUseCase: AlternarFavoritoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ListaContactoUiState>(ListaContactoUiState.Empty)
    val uiState: StateFlow<ListaContactoUiState> = _uiState.asStateFlow()

    fun getContactList(){
        viewModelScope.launch {
            _uiState.value = ListaContactoUiState.Loading
            when(val response = obtenerContactos()) {
                is Result.Error -> {
                    _uiState.value = ListaContactoUiState.Error(message = response.message)
                }
                is Result.Success -> {
                    _uiState.value = ListaContactoUiState.Success(
                        contactos = response.data
                    )
                }
            }
        }
    }

    fun alternarFavorito(id: String) {
        viewModelScope.launch {
            when (val resultado = alternarFavoritoUseCase(id)) {
                is Result.Success -> _uiState.update { current ->
                    if (current is ListaContactoUiState.Success) {
                        current.copy(
                            contactos = current.contactos.map {
                                if (it.id == resultado.data.id) resultado.data else it
                            }
                        )
                    } else current
                }
                // En error conservamos la lista tal cual; el favorito no cambio.
                is Result.Error -> Unit
            }
        }
    }

    fun onQueryChange(query: String) {
        _uiState.update { current ->
            if (current is ListaContactoUiState.Success) current.copy(query = query)
            else current
        }
    }

    fun onClear(){
        _uiState.update { current ->
            if (current is ListaContactoUiState.Success) current.copy(query = "")
            else current
        }
    }


}

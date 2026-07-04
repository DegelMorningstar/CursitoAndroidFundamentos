package com.yaeldev.cursitodefundamentosandroid.presentation.listaContacto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yaeldev.cursitodefundamentosandroid.domain.repositories.ContactoRepository
import com.yaeldev.cursitodefundamentosandroid.util.Result
import com.yaeldev.cursitodefundamentosandroid.util.toMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class ListaContactoViewModelFactory(
    private val repository: ContactoRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ListaContactoViewModel::class.java)) {
            return ListaContactoViewModel(repository) as T
        }
        throw IllegalArgumentException("No conozco este viewmodel")
    }

}

class ListaContactoViewModel(val repository: ContactoRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ListaContactoUiState>(ListaContactoUiState.Empty)
    val uiState: StateFlow<ListaContactoUiState> = _uiState.asStateFlow()

    fun getContactList(){
        viewModelScope.launch {
            _uiState.value = ListaContactoUiState.Loading
            when(val response = repository.obtenerTodos()) {
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

    fun alternarFavorito(id: Int) {

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
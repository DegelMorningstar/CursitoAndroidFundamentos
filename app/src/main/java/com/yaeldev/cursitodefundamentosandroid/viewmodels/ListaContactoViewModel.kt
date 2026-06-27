package com.yaeldev.cursitodefundamentosandroid.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yaeldev.cursitodefundamentosandroid.data.ContactoRepository
import com.yaeldev.cursitodefundamentosandroid.data.ContactosMuestra
import com.yaeldev.cursitodefundamentosandroid.views.listaContacto.ListaContactoUiState
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

    private val _uiState = MutableStateFlow(ListaContactoUiState())
    val uiState: StateFlow<ListaContactoUiState> = _uiState.asStateFlow()

    fun showLoading(show: Boolean){
        _uiState.update { state ->
            state.copy(isLoading = show)
        }
    }

    fun getContactList(){
        viewModelScope.launch {
            showLoading(true)
            delay(3000.milliseconds)
            showLoading(false)
            val contactos = repository.obtenerTodos()
            _uiState.update { state ->
                state.copy(contactos = contactos)
            }
        }
    }

    fun onQueryChange(it:String) {
        _uiState.update { state ->
            state.copy(query = it)
        }
    }

    fun onClear(){
        _uiState.update { state ->
            state.copy(query = "")
        }
    }


}
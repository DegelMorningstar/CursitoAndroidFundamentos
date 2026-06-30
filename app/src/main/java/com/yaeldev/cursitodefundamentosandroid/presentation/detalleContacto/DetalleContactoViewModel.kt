package com.yaeldev.cursitodefundamentosandroid.presentation.detalleContacto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yaeldev.cursitodefundamentosandroid.domain.repositories.ContactoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetalleContactoViewModelFactory(
    private val repository: ContactoRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetalleContactoViewModel::class.java)) {
            return DetalleContactoViewModel(repository) as T
        }
        throw IllegalArgumentException("No conozco este viewmodel")
    }

}

class DetalleContactoViewModel(val repository: ContactoRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(DetalleContactoUiState())
    val uiState: StateFlow<DetalleContactoUiState> = _uiState.asStateFlow()

    private var contactoId: Int = 0

    fun cargarContacto(id: Int) {
        contactoId = id
        viewModelScope.launch {
            val contacto = repository.obtenerPorId(id) ?: return@launch
            _uiState.update { state ->
                state.copy(
                    fullName = contacto.nombreCompleto,
                    initials = contacto.iniciales,
                    company = contacto.company,
                    phone = contacto.phone,
                    email = contacto.email,
                    esFavorito = contacto.favorite
                )
            }
        }
    }

    fun alternarFavorito() {
        viewModelScope.launch {
            val contacto = repository.alternarFavorito(contactoId)
            _uiState.update { state -> state.copy(esFavorito = contacto.favorite) }
        }
    }

    fun eliminar(onEliminado: () -> Unit) {
        viewModelScope.launch {
            repository.eliminar(contactoId)
            onEliminado()
        }
    }

}

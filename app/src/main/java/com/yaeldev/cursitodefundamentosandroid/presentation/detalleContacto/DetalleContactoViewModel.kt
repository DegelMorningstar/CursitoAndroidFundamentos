package com.yaeldev.cursitodefundamentosandroid.presentation.detalleContacto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yaeldev.cursitodefundamentosandroid.domain.usecases.AlternarFavoritoUseCase
import com.yaeldev.cursitodefundamentosandroid.domain.usecases.EliminarContactoUseCase
import com.yaeldev.cursitodefundamentosandroid.domain.usecases.ObtenerContactoPorIdUseCase
import com.yaeldev.cursitodefundamentosandroid.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetalleContactoViewModelFactory(
    private val obtenerContactoPorId: ObtenerContactoPorIdUseCase,
    private val alternarFavorito: AlternarFavoritoUseCase,
    private val eliminarContacto: EliminarContactoUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetalleContactoViewModel::class.java)) {
            return DetalleContactoViewModel(
                obtenerContactoPorId,
                alternarFavorito,
                eliminarContacto
            ) as T
        }
        throw IllegalArgumentException("No conozco este viewmodel")
    }

}

class DetalleContactoViewModel(
    private val obtenerContactoPorId: ObtenerContactoPorIdUseCase,
    private val alternarFavoritoUseCase: AlternarFavoritoUseCase,
    private val eliminarContacto: EliminarContactoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetalleContactoUiState())
    val uiState: StateFlow<DetalleContactoUiState> = _uiState.asStateFlow()

    private var contactoId: String = ""

    fun cargarContacto(id: String) {
        contactoId = id
        viewModelScope.launch {
            when (val resultado = obtenerContactoPorId(id)) {
                is Result.Success -> {
                    val contacto = resultado.data ?: return@launch
                    _uiState.update { state ->
                        state.copy(
                            fullName = contacto.nombreCompleto,
                            initials = contacto.iniciales,
                            company = contacto.company,
                            phone = contacto.phone,
                            email = contacto.email,
                            esFavorito = contacto.favorite,
                            error = null
                        )
                    }
                }
                is Result.Error -> _uiState.update { it.copy(error = resultado.message) }
            }
        }
    }

    fun alternarFavorito() {
        viewModelScope.launch {
            when (val resultado = alternarFavoritoUseCase(contactoId)) {
                is Result.Success -> _uiState.update { it.copy(esFavorito = resultado.data.favorite) }
                is Result.Error -> _uiState.update { it.copy(error = resultado.message) }
            }
        }
    }

    fun eliminar(onEliminado: () -> Unit) {
        viewModelScope.launch {
            when (val resultado = eliminarContacto(contactoId)) {
                is Result.Success -> onEliminado()
                is Result.Error -> _uiState.update { it.copy(error = resultado.message) }
            }
        }
    }

}

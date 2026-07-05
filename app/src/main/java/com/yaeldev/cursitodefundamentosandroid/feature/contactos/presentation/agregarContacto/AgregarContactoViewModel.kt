package com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.agregarContacto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.models.Contacto
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases.AgregarContactoUseCase
import com.yaeldev.cursitodefundamentosandroid.core.network.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AgregarContactoViewModelFactory(
    private val agregarContacto: AgregarContactoUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AgregarContactoViewModel::class.java)) {
            return AgregarContactoViewModel(agregarContacto) as T
        }
        throw IllegalArgumentException("No conozco este viewmodel")
    }

}

class AgregarContactoViewModel(
    private val agregarContacto: AgregarContactoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AgregarContactoUiState())
    val uiState: StateFlow<AgregarContactoUiState> = _uiState.asStateFlow()

    fun onNombreChange(it: String) {
        _uiState.update { state -> state.copy(nombre = it, errorNombre = null) }
    }

    fun onApellidoChange(it: String) {
        _uiState.update { state -> state.copy(apellido = it) }
    }

    fun onTelefonoChange(it: String) {
        _uiState.update { state -> state.copy(telefono = it, errorTelefono = null) }
    }

    fun onCorreoChange(it: String) {
        _uiState.update { state -> state.copy(correo = it) }
    }

    fun onEmpresaChange(it: String) {
        _uiState.update { state -> state.copy(empresa = it) }
    }

    fun guardar(onGuardado: () -> Unit) {
        val estado = _uiState.value
        val errorNombre = if (estado.nombre.isBlank()) "El nombre es obligatorio" else null
        val errorTelefono = if (estado.telefono.isBlank()) "El telefono es obligatorio" else null
        if (errorNombre != null || errorTelefono != null) {
            _uiState.update { state ->
                state.copy(errorNombre = errorNombre, errorTelefono = errorTelefono)
            }
            return
        }
        viewModelScope.launch {
            _uiState.update { state -> state.copy(error = null) }
            val resultado = agregarContacto(
                Contacto(
                    first = estado.nombre,
                    last = estado.apellido,
                    phone = estado.telefono,
                    email = estado.correo,
                    company = estado.empresa
                )
            )
            when (resultado) {
                is Result.Success -> onGuardado()
                is Result.Error -> _uiState.update { state -> state.copy(error = resultado.message) }
            }
        }
    }

}

package com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.editarContacto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.models.Contacto
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases.ActualizarContactoUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases.EliminarContactoUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.usecases.ObtenerContactoPorIdUseCase
import com.yaeldev.cursitodefundamentosandroid.core.network.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditarContactoViewModelFactory(
    private val obtenerContactoPorId: ObtenerContactoPorIdUseCase,
    private val actualizarContacto: ActualizarContactoUseCase,
    private val eliminarContacto: EliminarContactoUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditarContactoViewModel::class.java)) {
            return EditarContactoViewModel(
                obtenerContactoPorId,
                actualizarContacto,
                eliminarContacto
            ) as T
        }
        throw IllegalArgumentException("No conozco este viewmodel")
    }

}

class EditarContactoViewModel(
    private val obtenerContactoPorId: ObtenerContactoPorIdUseCase,
    private val actualizarContacto: ActualizarContactoUseCase,
    private val eliminarContacto: EliminarContactoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditarContactoUiState())
    val uiState: StateFlow<EditarContactoUiState> = _uiState.asStateFlow()

    // Guardamos el contacto original para preservar campos que el formulario no
    // edita (id, favorito) al actualizar.
    private var contactoActual: Contacto? = null

    fun cargarContacto(id: String) {
        viewModelScope.launch {
            when (val resultado = obtenerContactoPorId(id)) {
                is Result.Success -> {
                    val contacto = resultado.data ?: return@launch
                    contactoActual = contacto
                    _uiState.update { state ->
                        state.copy(
                            nombre = contacto.first,
                            apellido = contacto.last,
                            telefono = contacto.phone,
                            correo = contacto.email,
                            empresa = contacto.company,
                            error = null
                        )
                    }
                }
                is Result.Error -> _uiState.update { it.copy(error = resultado.message) }
            }
        }
    }

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
        val original = contactoActual ?: return
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
            val resultado = actualizarContacto(
                original.copy(
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

    fun eliminar(onEliminado: () -> Unit) {
        val id = contactoActual?.id ?: return
        viewModelScope.launch {
            when (val resultado = eliminarContacto(id)) {
                is Result.Success -> onEliminado()
                is Result.Error -> _uiState.update { it.copy(error = resultado.message) }
            }
        }
    }

}

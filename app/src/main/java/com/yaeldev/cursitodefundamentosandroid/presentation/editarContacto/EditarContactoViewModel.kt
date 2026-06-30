package com.yaeldev.cursitodefundamentosandroid.presentation.editarContacto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yaeldev.cursitodefundamentosandroid.domain.models.Contacto
import com.yaeldev.cursitodefundamentosandroid.domain.repositories.ContactoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditarContactoViewModelFactory(
    private val repository: ContactoRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditarContactoViewModel::class.java)) {
            return EditarContactoViewModel(repository) as T
        }
        throw IllegalArgumentException("No conozco este viewmodel")
    }

}

class EditarContactoViewModel(val repository: ContactoRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(EditarContactoUiState())
    val uiState: StateFlow<EditarContactoUiState> = _uiState.asStateFlow()

    // Guardamos el contacto original para preservar campos que el formulario no
    // edita (id, favorito) al actualizar.
    private var contactoActual: Contacto? = null

    fun cargarContacto(id: Int) {
        viewModelScope.launch {
            val contacto = repository.obtenerPorId(id) ?: return@launch
            contactoActual = contacto
            _uiState.update { state ->
                state.copy(
                    nombre = contacto.first,
                    apellido = contacto.last,
                    telefono = contacto.phone,
                    correo = contacto.email,
                    empresa = contacto.company
                )
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
            repository.actualizar(
                original.copy(
                    first = estado.nombre,
                    last = estado.apellido,
                    phone = estado.telefono,
                    email = estado.correo,
                    company = estado.empresa
                )
            )
            onGuardado()
        }
    }

    fun eliminar(onEliminado: () -> Unit) {
        val id = contactoActual?.id ?: return
        viewModelScope.launch {
            repository.eliminar(id)
            onEliminado()
        }
    }

}

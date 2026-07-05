package com.yaeldev.cursitodefundamentosandroid.feature.perfil.presentation.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.models.EstadoUsuario
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.usecases.ActualizarPerfilUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.usecases.CerrarSesionUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.usecases.ObtenerPerfilUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.usecases.ObtenerUsuarioActualUseCase
import com.yaeldev.cursitodefundamentosandroid.core.network.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class PerfilViewModel(
    obtenerUsuarioActual: ObtenerUsuarioActualUseCase,
    private val obtenerPerfil: ObtenerPerfilUseCase,
    private val actualizarPerfil: ActualizarPerfilUseCase,
    private val cerrarSesionUseCase: CerrarSesionUseCase
) : ViewModel() {

    // Valores inmediatos de FirebaseAuth (nombre/email); foto/estado llegan async.
    private val _uiState = MutableStateFlow(
        obtenerUsuarioActual().let { usuario ->
            PerfilUiState(nombre = usuario?.nombre.orEmpty(), email = usuario?.email.orEmpty())
        }
    )
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    init {
        cargarPerfil()
    }

    private fun cargarPerfil() {
        viewModelScope.launch {
            when (val resultado = obtenerPerfil()) {
                is Result.Success -> resultado.data?.let { usuario ->
                    _uiState.update {
                        it.copy(
                            nombre = usuario.nombre,
                            email = usuario.email,
                            foto = usuario.foto,
                            estado = usuario.estado
                        )
                    }
                }
                is Result.Error -> _uiState.update { it.copy(error = resultado.message) }
            }
        }
    }

    fun onNombreChange(nuevo: String) {
        _uiState.update { it.copy(nombre = nuevo, mensaje = null, error = null) }
    }

    fun onEstadoChange(nuevo: EstadoUsuario) {
        _uiState.update { it.copy(estado = nuevo, mensaje = null, error = null) }
    }

    /** La recibe el Root tras elegir y comprimir la imagen a base64. */
    fun onFotoSeleccionada(base64: String) {
        _uiState.update { it.copy(foto = base64, mensaje = null, error = null) }
    }

    fun mostrarError(mensaje: String) {
        _uiState.update { it.copy(error = mensaje) }
    }

    /** Limpia el mensaje de exito una vez mostrado en el snackbar. */
    fun mensajeConsumido() {
        _uiState.update { it.copy(mensaje = null) }
    }

    fun guardar() {
        val estado = _uiState.value
        if (estado.nombre.isBlank()) {
            _uiState.update { it.copy(error = "El nombre es obligatorio") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(guardando = true, mensaje = null, error = null) }
            when (val resultado = actualizarPerfil(estado.nombre.trim(), estado.foto, estado.estado)) {
                is Result.Success -> _uiState.update {
                    it.copy(guardando = false, mensaje = "Perfil actualizado")
                }
                is Result.Error -> _uiState.update {
                    it.copy(guardando = false, error = resultado.message)
                }
            }
        }
    }

    fun cerrarSesion(onSesionCerrada: () -> Unit) {
        cerrarSesionUseCase()
        onSesionCerrada()
    }
}

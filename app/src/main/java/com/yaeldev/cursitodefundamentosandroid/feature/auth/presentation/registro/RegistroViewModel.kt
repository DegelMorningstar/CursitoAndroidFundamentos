package com.yaeldev.cursitodefundamentosandroid.feature.auth.presentation.registro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.usecases.RegistrarUseCase
import com.yaeldev.cursitodefundamentosandroid.core.network.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistroViewModel(
    private val registrar: RegistrarUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()

    fun onNombreChange(it: String) {
        _uiState.update { state -> state.copy(nombre = it, errorNombre = null, error = null) }
    }

    fun onApellidoChange(it: String) {
        _uiState.update { state -> state.copy(apellido = it, error = null) }
    }

    fun onEmailChange(it: String) {
        _uiState.update { state -> state.copy(email = it, errorEmail = null, error = null) }
    }

    fun onPasswordChange(it: String) {
        _uiState.update { state -> state.copy(password = it, errorPassword = null, error = null) }
    }

    fun onConfirmarPasswordChange(it: String) {
        _uiState.update { state -> state.copy(confirmarPassword = it, errorConfirmar = null, error = null) }
    }

    fun registrar(onRegistrado: () -> Unit) {
        val estado = _uiState.value
        val errorNombre = if (estado.nombre.isBlank()) "El nombre es obligatorio" else null
        val errorEmail = if (estado.email.isBlank()) "El correo es obligatorio" else null
        val errorPassword = when {
            estado.password.isBlank() -> "La contraseña es obligatoria"
            estado.password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            else -> null
        }
        val errorConfirmar = if (estado.confirmarPassword != estado.password)
            "Las contraseñas no coinciden" else null

        if (errorNombre != null || errorEmail != null ||
            errorPassword != null || errorConfirmar != null
        ) {
            _uiState.update { state ->
                state.copy(
                    errorNombre = errorNombre,
                    errorEmail = errorEmail,
                    errorPassword = errorPassword,
                    errorConfirmar = errorConfirmar
                )
            }
            return
        }
        viewModelScope.launch {
            _uiState.update { state -> state.copy(cargando = true, error = null) }
            val resultado = registrar(
                estado.nombre.trim(),
                estado.apellido.trim(),
                estado.email.trim(),
                estado.password
            )
            when (resultado) {
                is Result.Success -> {
                    _uiState.update { state -> state.copy(cargando = false) }
                    onRegistrado()
                }
                is Result.Error -> _uiState.update { state ->
                    state.copy(cargando = false, error = resultado.message)
                }
            }
        }
    }
}

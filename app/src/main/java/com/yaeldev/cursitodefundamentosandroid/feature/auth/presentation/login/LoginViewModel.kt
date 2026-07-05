package com.yaeldev.cursitodefundamentosandroid.feature.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.usecases.IniciarSesionUseCase
import com.yaeldev.cursitodefundamentosandroid.core.network.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModelFactory(
    private val iniciarSesion: IniciarSesionUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(iniciarSesion) as T
        }
        throw IllegalArgumentException("No conozco este viewmodel")
    }
}

class LoginViewModel(
    private val iniciarSesion: IniciarSesionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(it: String) {
        _uiState.update { state -> state.copy(email = it, errorEmail = null, error = null) }
    }

    fun onPasswordChange(it: String) {
        _uiState.update { state -> state.copy(password = it, errorPassword = null, error = null) }
    }

    fun iniciarSesion(onAutenticado: () -> Unit) {
        val estado = _uiState.value
        val errorEmail = if (estado.email.isBlank()) "El correo es obligatorio" else null
        val errorPassword = if (estado.password.isBlank()) "La contraseña es obligatoria" else null
        if (errorEmail != null || errorPassword != null) {
            _uiState.update { state ->
                state.copy(errorEmail = errorEmail, errorPassword = errorPassword)
            }
            return
        }
        viewModelScope.launch {
            _uiState.update { state -> state.copy(cargando = true, error = null) }
            val resultado = iniciarSesion(estado.email.trim(), estado.password)
            when (resultado) {
                is Result.Success -> {
                    _uiState.update { state -> state.copy(cargando = false) }
                    onAutenticado()
                }
                is Result.Error -> _uiState.update { state ->
                    state.copy(cargando = false, error = resultado.message)
                }
            }
        }
    }
}

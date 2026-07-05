package com.yaeldev.cursitodefundamentosandroid.feature.perfil.presentation.detallePerfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.usecases.ObtenerUsuarioPorIdUseCase
import com.yaeldev.cursitodefundamentosandroid.core.network.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetallePerfilViewModel(
    private val obtenerUsuarioPorId: ObtenerUsuarioPorIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetallePerfilUiState>(DetallePerfilUiState.Loading)
    val uiState: StateFlow<DetallePerfilUiState> = _uiState.asStateFlow()

    fun cargarPerfil(uid: String) {
        viewModelScope.launch {
            _uiState.update { DetallePerfilUiState.Loading }
            _uiState.update {
                when (val resultado = obtenerUsuarioPorId(uid)) {
                    is Result.Success -> {
                        val usuario = resultado.data ?: return@update DetallePerfilUiState.Empty
                        DetallePerfilUiState.Success(
                            nombre = usuario.nombre,
                            email = usuario.email,
                            foto = usuario.foto,
                            estado = usuario.estado
                        )
                    }
                    is Result.Error -> DetallePerfilUiState.Error(resultado.message)
                }
            }
        }
    }
}

package com.yaeldev.cursitodefundamentosandroid.feature.perfil.presentation.detallePerfil

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.models.EstadoUsuario

sealed interface DetallePerfilUiState {
    data object Loading : DetallePerfilUiState
    data object Empty : DetallePerfilUiState                 // usuario no encontrado
    data class Error(val message: String) : DetallePerfilUiState
    data class Success(
        val nombre: String,
        val email: String,
        val foto: String,
        val estado: EstadoUsuario
    ) : DetallePerfilUiState {
        val iniciales: String
            get() = nombre.split(" ")
                .filter { it.isNotBlank() }
                .take(2)
                .map { it.first().uppercaseChar() }
                .joinToString("")
                .ifEmpty { "?" }
    }
}

data class DetallePerfilActions(
    val onBack: () -> Unit = {}
)

class DetallePerfilPreviewParameterProvider : PreviewParameterProvider<DetallePerfilUiState> {
    override val values: Sequence<DetallePerfilUiState>
        get() = sequenceOf(
            DetallePerfilUiState.Loading,
            DetallePerfilUiState.Empty,
            DetallePerfilUiState.Error("No se pudo cargar el perfil."),
            DetallePerfilUiState.Success(
                nombre = "Ruben Estrada",
                email = "ruben@correo.com",
                foto = "",
                estado = EstadoUsuario.EnVacaciones
            )
        )
}

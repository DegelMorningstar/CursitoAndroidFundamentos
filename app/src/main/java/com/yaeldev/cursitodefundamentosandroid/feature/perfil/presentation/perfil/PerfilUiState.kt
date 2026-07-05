package com.yaeldev.cursitodefundamentosandroid.feature.perfil.presentation.perfil

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.models.EstadoUsuario

data class PerfilUiState(
    val nombre: String = "",
    val email: String = "",
    val foto: String = "",                       // base64 del avatar ("" = sin foto)
    val estado: EstadoUsuario = EstadoUsuario.Disponible,
    val temaOscuro: Boolean = false,
    val guardando: Boolean = false,
    val mensaje: String? = null,                 // feedback "Perfil actualizado"
    val error: String? = null
) {
    /** Iniciales para el avatar cuando no hay foto. */
    val iniciales: String
        get() = nombre.split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .map { it.first().uppercaseChar() }
            .joinToString("")
            .ifEmpty { "?" }
}

data class PerfilActions(
    val onNombreChange: (String) -> Unit = {},
    val onEstadoChange: (EstadoUsuario) -> Unit = {},
    val onCambiarFoto: () -> Unit = {},
    val onGuardar: () -> Unit = {},
    val onToggleTema: (Boolean) -> Unit = {},
    val onCerrarSesion: () -> Unit = {},
    val onNavigateToContactos: () -> Unit = {},
    val onNavigateToFavoritos: () -> Unit = {},
    val onNavigateToChats: () -> Unit = {}
)

class PerfilPreviewParameterProvider : PreviewParameterProvider<PerfilUiState> {
    override val values: Sequence<PerfilUiState>
        get() = sequenceOf(
            PerfilUiState(nombre = "Yael Montes", email = "yael_montes@outlook.com"),
            PerfilUiState(
                nombre = "Yael Montes",
                email = "yael_montes@outlook.com",
                estado = EstadoUsuario.EnVacaciones,
                temaOscuro = true
            ),
            PerfilUiState(nombre = "Yael Montes", email = "yael_montes@outlook.com", guardando = true)
        )
}

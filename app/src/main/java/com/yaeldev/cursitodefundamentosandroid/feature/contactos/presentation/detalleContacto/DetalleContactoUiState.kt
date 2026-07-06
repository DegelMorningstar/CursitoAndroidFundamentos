package com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.detalleContacto

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.yaeldev.cursitodefundamentosandroid.core.ui.theme.pink

sealed interface DetalleContactoUiState {
    data object Loading : DetalleContactoUiState
    data object Empty : DetalleContactoUiState
    data class Error(val message: String) : DetalleContactoUiState
    data class Success(
        val fullName: String = "",
        val initials: String = "?",
        val company: String = "",
        val phone: String = "",
        val email: String = "",
        val esFavorito: Boolean = false,
        val avatarColor: Color = pink,
        val error: String? = null,           // error de operacion (eliminar/favorito), se muestra sobre el contacto
        // El contacto no es usuario de la app: se ofrece chatear por SMS/WhatsApp/Telegram.
        val mostrarOpcionesExternas: Boolean = false
    ) : DetalleContactoUiState
}

data class DetalleContactoActions(
    val onBack: () -> Unit = {},
    val onEdit: () -> Unit = {},
    val onShare: (String) -> Unit = {},
    val onDelete: () -> Unit = {},
    val onCall: (String) -> Unit = {},
    val onEmail: (String) -> Unit = {},
    val onToggleFavorite: () -> Unit = {},
    val onMensaje: () -> Unit = {},
    val onSms: (String) -> Unit = {},
    val onWhatsApp: (String) -> Unit = {},
    val onTelegram: (String) -> Unit = {},
    val onCerrarOpcionesExternas: () -> Unit = {}
)

class DetalleContactoPreviewParameterProvider : PreviewParameterProvider<DetalleContactoUiState> {
    override val values: Sequence<DetalleContactoUiState>
        get() = sequenceOf(
            DetalleContactoUiState.Loading,
            DetalleContactoUiState.Empty,
            DetalleContactoUiState.Error("No se pudo cargar el contacto."),
            DetalleContactoUiState.Success(
                fullName = "Yael Montes Camacho",
                initials = "YM",
                company = "YaelDev",
                phone = "+52 7771234568",
                email = "yael@correo.com",
                esFavorito = true
            ),
            DetalleContactoUiState.Success(
                fullName = "Ruben Estrada Zavala",
                initials = "RE",
                company = "AsTecI",
                phone = "5512345678",
                email = "ruben@asteci.com"
            )
        )
}

package com.yaeldev.cursitodefundamentosandroid.presentation.detalleContacto

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.yaeldev.cursitodefundamentosandroid.presentation.theme.pink

data class DetalleContactoUiState(
    val fullName: String = "",
    val initials: String = "?",
    val company: String = "",
    val phone: String = "",
    val email: String = "",
    val esFavorito: Boolean = false,
    val avatarColor: Color = pink
)

data class DetalleContactoActions(
    val onBack: () -> Unit = {},
    val onEdit: () -> Unit = {},
    val onShare: () -> Unit = {},
    val onDelete: () -> Unit = {}
)

class DetalleContactoPreviewParameterProvider : PreviewParameterProvider<DetalleContactoUiState> {
    override val values: Sequence<DetalleContactoUiState>
        get() = sequenceOf(
            DetalleContactoUiState(
                fullName = "Yael Montes Camacho",
                initials = "YM",
                company = "YaelDev",
                phone = "+52 7771234568",
                email = "yael@correo.com",
                esFavorito = true
            ),
            DetalleContactoUiState(
                fullName = "Ruben Estrada Zavala",
                initials = "RE",
                company = "AsTecI",
                phone = "5512345678",
                email = "ruben@asteci.com"
            )
        )
}

package com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.agregarContacto

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

data class AgregarContactoUiState(
    val nombre: String = "",
    val apellido: String = "",
    val telefono: String = "",
    val correo: String = "",
    val empresa: String = "",
    val errorNombre: String? = null,
    val errorTelefono: String? = null,
    val error: String? = null            // error de la operacion (ej. fallo al guardar)
)

data class AgregarContactoActions(
    val onClose: () -> Unit = {},
    val onGuardar: () -> Unit = {},
    val onNombreChange: (String) -> Unit = {},
    val onApellidoChange: (String) -> Unit = {},
    val onTelefonoChange: (String) -> Unit = {},
    val onCorreoChange: (String) -> Unit = {},
    val onEmpresaChange: (String) -> Unit = {}
)

class AgregarContactoPreviewParameterProvider : PreviewParameterProvider<AgregarContactoUiState> {
    override val values: Sequence<AgregarContactoUiState>
        get() = sequenceOf(
            AgregarContactoUiState(),
            AgregarContactoUiState(
                nombre = "Yael",
                apellido = "Montes Camacho",
                telefono = "7771234568",
                correo = "yael@correo.com",
                empresa = "YaelDev"
            )
        )
}

package com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.editarContacto

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

data class EditarContactoUiState(
    val nombre: String = "",
    val apellido: String = "",
    val telefono: String = "",
    val correo: String = "",
    val empresa: String = "",
    val errorNombre: String? = null,
    val errorTelefono: String? = null,
    val error: String? = null            // error de la operacion (guardar/eliminar/cargar)
)

data class EditarContactoActions(
    val onClose: () -> Unit = {},
    val onGuardar: () -> Unit = {},
    val onDelete: () -> Unit = {},
    val onNombreChange: (String) -> Unit = {},
    val onApellidoChange: (String) -> Unit = {},
    val onTelefonoChange: (String) -> Unit = {},
    val onCorreoChange: (String) -> Unit = {},
    val onEmpresaChange: (String) -> Unit = {}
)

class EditarContactoPreviewParameterProvider : PreviewParameterProvider<EditarContactoUiState> {
    override val values: Sequence<EditarContactoUiState>
        get() = sequenceOf(
            EditarContactoUiState(
                nombre = "Yael",
                apellido = "Montes Camacho",
                telefono = "7771234568",
                correo = "yael@correo.com",
                empresa = "YaelDev"
            ),
            EditarContactoUiState()
        )
}

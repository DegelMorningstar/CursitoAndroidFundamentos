package com.yaeldev.cursitodefundamentosandroid.feature.auth.presentation.registro

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

data class RegistroUiState(
    val nombre: String = "",
    val apellido: String = "",
    val email: String = "",
    val password: String = "",
    val confirmarPassword: String = "",
    val errorNombre: String? = null,
    val errorEmail: String? = null,
    val errorPassword: String? = null,
    val errorConfirmar: String? = null,
    val error: String? = null,      // error de la operacion (ej. correo ya en uso)
    val cargando: Boolean = false
)

data class RegistroActions(
    val onNombreChange: (String) -> Unit = {},
    val onApellidoChange: (String) -> Unit = {},
    val onEmailChange: (String) -> Unit = {},
    val onPasswordChange: (String) -> Unit = {},
    val onConfirmarPasswordChange: (String) -> Unit = {},
    val onRegistrar: () -> Unit = {},
    val onIrALogin: () -> Unit = {}
)

class RegistroPreviewParameterProvider : PreviewParameterProvider<RegistroUiState> {
    override val values: Sequence<RegistroUiState>
        get() = sequenceOf(
            RegistroUiState(),
            RegistroUiState(
                nombre = "Yael",
                apellido = "Montes",
                email = "yael@correo.com",
                password = "secreto123",
                confirmarPassword = "secreto123"
            ),
            RegistroUiState(cargando = true),
            RegistroUiState(
                nombre = "Yael",
                apellido = "Montes",
                email = "yael@correo.com",
                error = "Ese correo ya esta registrado."
            )
        )
}

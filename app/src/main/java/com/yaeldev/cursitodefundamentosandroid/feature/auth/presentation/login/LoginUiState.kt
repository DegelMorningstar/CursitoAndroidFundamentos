package com.yaeldev.cursitodefundamentosandroid.feature.auth.presentation.login

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val errorEmail: String? = null,
    val errorPassword: String? = null,
    val error: String? = null,      // error de la operacion (ej. credenciales invalidas)
    val cargando: Boolean = false
)

data class LoginActions(
    val onEmailChange: (String) -> Unit = {},
    val onPasswordChange: (String) -> Unit = {},
    val onIniciarSesion: () -> Unit = {},
    val onIrARegistro: () -> Unit = {}
)

class LoginPreviewParameterProvider : PreviewParameterProvider<LoginUiState> {
    override val values: Sequence<LoginUiState>
        get() = sequenceOf(
            LoginUiState(),
            LoginUiState(
                email = "yael@correo.com",
                password = "secreto123"
            ),
            LoginUiState(cargando = true),
            LoginUiState(
                email = "yael@correo.com",
                error = "Correo o contraseña incorrectos."
            )
        )
}

package com.yaeldev.cursitodefundamentosandroid.feature.auth.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.yaeldev.cursitodefundamentosandroid.core.ui.components.FormField
import com.yaeldev.cursitodefundamentosandroid.core.ui.components.MensajeError
import com.yaeldev.cursitodefundamentosandroid.core.ui.theme.AppTheme

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    uiState: LoginUiState,
    actions: LoginActions
) {
    Scaffold(modifier = modifier) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Iniciar sesion",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(24.dp))

            MensajeError(mensaje = uiState.error)
            if (uiState.error != null) Spacer(modifier = Modifier.height(8.dp))

            FormField(
                label = "Correo",
                value = uiState.email,
                placeholder = "tucorreo@ejemplo.com",
                required = true,
                error = uiState.errorEmail,
                keyboardType = KeyboardType.Email,
                onValueChange = actions.onEmailChange
            )
            FormField(
                label = "Contraseña",
                value = uiState.password,
                placeholder = "Tu contraseña",
                required = true,
                error = uiState.errorPassword,
                keyboardType = KeyboardType.Password,
                isPassword = true,
                onValueChange = actions.onPasswordChange
            )

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = actions.onIniciarSesion,
                enabled = !uiState.cargando
            ) {
                if (uiState.cargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Entrar")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            TextButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = actions.onIrARegistro,
                enabled = !uiState.cargando
            ) {
                Text("¿No tienes cuenta? Registrate")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginPreview(
    @PreviewParameter(LoginPreviewParameterProvider::class) state: LoginUiState
) {
    AppTheme {
        LoginScreen(
            uiState = state,
            actions = LoginActions()
        )
    }
}

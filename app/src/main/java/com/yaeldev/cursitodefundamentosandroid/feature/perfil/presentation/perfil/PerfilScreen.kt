package com.yaeldev.cursitodefundamentosandroid.feature.perfil.presentation.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.yaeldev.cursitodefundamentosandroid.BuildConfig
import com.yaeldev.cursitodefundamentosandroid.core.ui.components.AppBottomBar
import com.yaeldev.cursitodefundamentosandroid.core.ui.components.AppTab
import com.yaeldev.cursitodefundamentosandroid.core.ui.components.AvatarImagen
import com.yaeldev.cursitodefundamentosandroid.core.ui.components.FormField
import com.yaeldev.cursitodefundamentosandroid.core.ui.components.MensajeError
import com.yaeldev.cursitodefundamentosandroid.core.ui.theme.AppTheme
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.models.EstadoUsuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    modifier: Modifier = Modifier,
    uiState: PerfilUiState,
    actions: PerfilActions
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Text(
                        text = "Perfil",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.displayLarge
                    )
                }
            )
        },
        bottomBar = {
            AppBottomBar(
                seleccionada = AppTab.Perfil,
                onContactos = actions.onNavigateToContactos,
                onFavoritos = actions.onNavigateToFavoritos,
                onChats = actions.onNavigateToChats,
                onPerfil = {}
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AvatarImagen(
                fotoBase64 = uiState.foto,
                iniciales = uiState.iniciales,
                size = 96.dp,
                modifier = Modifier.clickable(onClick = actions.onCambiarFoto)
            )
            TextButton(onClick = actions.onCambiarFoto) {
                Text(if (uiState.foto.isBlank()) "Agregar foto" else "Cambiar foto")
            }
            Text(
                text = uiState.email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))
            MensajeError(mensaje = uiState.error)

            DatosEditables(uiState = uiState, actions = actions)

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = actions.onGuardar,
                enabled = !uiState.guardando,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.guardando) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Guardar cambios")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "PREFERENCIAS",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            PreferenciaTema(activo = uiState.temaOscuro, onToggle = actions.onToggleTema)

            Spacer(modifier = Modifier.height(24.dp))
            OutlinedButton(
                onClick = actions.onCerrarSesion,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null,
                    modifier = Modifier.height(20.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Cerrar sesion")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DatosEditables(uiState: PerfilUiState, actions: PerfilActions) {
    FormField(
        label = "Nombre",
        value = uiState.nombre,
        placeholder = "Tu nombre",
        required = true,
        onValueChange = actions.onNombreChange
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = "ESTADO",
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        EstadoUsuario.entries.forEach { estado ->
            FilterChip(
                selected = uiState.estado == estado,
                onClick = { actions.onEstadoChange(estado) },
                label = { Text(estado.etiqueta) }
            )
        }
    }
}

@Composable
private fun PreferenciaTema(activo: Boolean, onToggle: (Boolean) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Tema oscuro",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (activo) "Activado" else "Desactivado",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(checked = activo, onCheckedChange = onToggle)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PerfilPreview(
    @PreviewParameter(PerfilPreviewParameterProvider::class) state: PerfilUiState
) {
    AppTheme(darkTheme = state.temaOscuro) {
        PerfilScreen(
            uiState = state,
            actions = PerfilActions()
        )
    }
}

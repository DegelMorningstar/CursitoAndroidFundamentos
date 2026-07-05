package com.yaeldev.cursitodefundamentosandroid.feature.perfil.presentation.detallePerfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.yaeldev.cursitodefundamentosandroid.core.ui.components.AvatarImagen
import com.yaeldev.cursitodefundamentosandroid.core.ui.theme.AppTheme
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.models.EstadoUsuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallePerfilScreen(
    modifier: Modifier = Modifier,
    uiState: DetallePerfilUiState,
    actions: DetallePerfilActions
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    IconButton(onClick = actions.onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                title = {
                    Text(text = "Perfil", style = MaterialTheme.typography.headlineLarge)
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (uiState) {
                DetallePerfilUiState.Loading ->
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                DetallePerfilUiState.Empty ->
                    Text(
                        text = "Perfil no encontrado",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.Center)
                    )

                is DetallePerfilUiState.Error ->
                    Text(text = uiState.message, modifier = Modifier.align(Alignment.Center))

                is DetallePerfilUiState.Success ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))
                        AvatarImagen(
                            fotoBase64 = uiState.foto,
                            iniciales = uiState.iniciales,
                            size = 128.dp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = uiState.nombre.ifBlank { "Sin nombre" },
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = uiState.email,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        BadgeEstado(estado = uiState.estado)
                    }
            }
        }
    }
}

@Composable
private fun BadgeEstado(estado: EstadoUsuario) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(colorEstado(estado), CircleShape)
            )
            Text(
                text = estado.etiqueta,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

private fun colorEstado(estado: EstadoUsuario): Color = when (estado) {
    EstadoUsuario.Disponible -> Color(0xFF16A34A)   // verde
    EstadoUsuario.Ocupado -> Color(0xFFDC2626)      // rojo
    EstadoUsuario.EnVacaciones -> Color(0xFF2563EB) // azul
}

@Preview(showBackground = true)
@Composable
private fun DetallePerfilPreview(
    @PreviewParameter(DetallePerfilPreviewParameterProvider::class) state: DetallePerfilUiState
) {
    AppTheme {
        DetallePerfilScreen(
            uiState = state,
            actions = DetallePerfilActions()
        )
    }
}

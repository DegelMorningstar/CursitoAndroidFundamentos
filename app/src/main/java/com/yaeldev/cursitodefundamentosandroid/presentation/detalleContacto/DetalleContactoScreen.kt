package com.yaeldev.cursitodefundamentosandroid.presentation.detalleContacto

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.yaeldev.cursitodefundamentosandroid.presentation.theme.AppTheme
import com.yaeldev.cursitodefundamentosandroid.presentation.components.ActionButton
import com.yaeldev.cursitodefundamentosandroid.presentation.components.DeleteButton
import com.yaeldev.cursitodefundamentosandroid.presentation.components.InfoCardRow
import com.yaeldev.cursitodefundamentosandroid.presentation.components.MensajeError

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleContactoScreen(
    modifier: Modifier = Modifier,
    uiState: DetalleContactoUiState,
    actions: DetalleContactoActions
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
                    Text(text = "Contacto", style = MaterialTheme.typography.headlineLarge)
                },
                actions = {
                    IconButton(onClick = actions.onEdit) {
                        Icon(Icons.Filled.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = actions.onShare) {
                        Icon(Icons.Filled.Share, contentDescription = "Compartir")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            HeroCard(
                fullName = uiState.fullName,
                initials = uiState.initials,
                company = uiState.company,
                esFavorito = uiState.esFavorito,
                avatarColor = uiState.avatarColor
            )
            Spacer(modifier = Modifier.height(16.dp))
            InfoCard(phone = uiState.phone, email = uiState.email, company = uiState.company)
            Spacer(modifier = Modifier.height(24.dp))
            MensajeError(mensaje = uiState.error)
            if (uiState.error != null) Spacer(modifier = Modifier.height(24.dp))
            DeleteButton(onClick = actions.onDelete)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun HeroCard(
    fullName: String,
    initials: String,
    company: String,
    esFavorito: Boolean,
    avatarColor: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(avatarColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials,
                    color = Color.White,
                    style = MaterialTheme.typography.displayMedium
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = fullName,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = company,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ActionButton(icon = Icons.Filled.Phone, label = "Llamar")
                ActionButton(icon = Icons.AutoMirrored.Filled.Send, label = "Mensaje")
                ActionButton(icon = Icons.Filled.Email, label = "Email")
                ActionButton(
                    icon = Icons.Filled.Star,
                    label = "Favorito"
                )
            }
        }
    }
}

@Composable
private fun InfoCard(phone: String, email: String, company: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
            InfoCardRow(icon = Icons.Filled.Phone, label = "Telefono", value = phone)
            InfoCardRow(icon = Icons.Filled.Email, label = "Correo", value = email)
            InfoCardRow(icon = Icons.Filled.Home, label = "Empresa", value = company)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetalleContactoPreview(
    @PreviewParameter(DetalleContactoPreviewParameterProvider::class) state: DetalleContactoUiState
) {
    AppTheme {
        DetalleContactoScreen(
            uiState = state,
            actions = DetalleContactoActions()
        )
    }
}

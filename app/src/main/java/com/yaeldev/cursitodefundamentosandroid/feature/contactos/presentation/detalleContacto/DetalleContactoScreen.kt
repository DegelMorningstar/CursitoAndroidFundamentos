package com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.detalleContacto

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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.yaeldev.cursitodefundamentosandroid.core.ui.theme.AppTheme
import com.yaeldev.cursitodefundamentosandroid.core.ui.theme.colorAvatar
import com.yaeldev.cursitodefundamentosandroid.core.ui.components.ActionButton
import com.yaeldev.cursitodefundamentosandroid.core.ui.components.ConfirmDialog
import com.yaeldev.cursitodefundamentosandroid.core.ui.components.DeleteButton
import com.yaeldev.cursitodefundamentosandroid.core.ui.components.InfoCardRow
import com.yaeldev.cursitodefundamentosandroid.core.ui.components.MensajeError

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleContactoScreen(
    modifier: Modifier = Modifier,
    uiState: DetalleContactoUiState,
    actions: DetalleContactoActions
) {
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }

    if (mostrarDialogoEliminar) {
        ConfirmDialog(
            onDismiss = { mostrarDialogoEliminar = false },
            onConfirm = {
                mostrarDialogoEliminar = false
                actions.onDelete()
            }
        )
    }

    val estadoExterno = uiState as? DetalleContactoUiState.Success
    if (estadoExterno?.mostrarOpcionesExternas == true) {
        OpcionesMensajeriaSheet(
            phone = estadoExterno.phone,
            onSms = actions.onSms,
            onWhatsApp = actions.onWhatsApp,
            onTelegram = actions.onTelegram,
            onDismiss = actions.onCerrarOpcionesExternas
        )
    }

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
                    if (uiState is DetalleContactoUiState.Success) {
                        IconButton(onClick = actions.onEdit) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar")
                        }
                        IconButton(onClick = { actions.onShare(textoParaCompartir(uiState)) }) {
                            Icon(Icons.Filled.Share, contentDescription = "Compartir")
                        }
                    }
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
                DetalleContactoUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                DetalleContactoUiState.Empty -> {
                    Text(
                        text = "Contacto no encontrado",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is DetalleContactoUiState.Error -> {
                    Text(
                        text = uiState.message,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is DetalleContactoUiState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        HeroCard(
                            fullName = uiState.fullName,
                            initials = uiState.initials,
                            company = uiState.company,
                            esFavorito = uiState.esFavorito,
                            avatarColor = colorAvatar(uiState.fullName),
                            onCall = { actions.onCall(uiState.phone) },
                            onEmail = { actions.onEmail(uiState.email) },
                            onMensaje = actions.onMensaje,
                            onToggleFavorite = actions.onToggleFavorite
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        InfoCard(phone = uiState.phone, email = uiState.email, company = uiState.company)
                        Spacer(modifier = Modifier.height(24.dp))
                        MensajeError(mensaje = uiState.error)
                        if (uiState.error != null) Spacer(modifier = Modifier.height(24.dp))
                        DeleteButton(onClick = { mostrarDialogoEliminar = true })
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroCard(
    fullName: String,
    initials: String,
    company: String,
    esFavorito: Boolean,
    avatarColor: Color,
    onCall: () -> Unit = {},
    onEmail: () -> Unit = {},
    onMensaje: () -> Unit = {},
    onToggleFavorite: () -> Unit = {}
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
                ActionButton(icon = Icons.Filled.Phone, label = "Llamar", onClick = onCall)
                ActionButton(icon = Icons.AutoMirrored.Filled.Send, label = "Mensaje", onClick = onMensaje)
                ActionButton(icon = Icons.Filled.Email, label = "Email", onClick = onEmail)
                ActionButton(
                    icon = Icons.Filled.Star,
                    label = "Favorito",
                    tint = if (esFavorito) Color(0xFFF59F00) else MaterialTheme.colorScheme.primary,
                    onClick = onToggleFavorite
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

/**
 * Hoja inferior que se muestra cuando el contacto no es usuario de la app: ofrece
 * iniciar la conversacion por una mensajeria externa usando su telefono.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OpcionesMensajeriaSheet(
    phone: String,
    onSms: (String) -> Unit,
    onWhatsApp: (String) -> Unit,
    onTelegram: (String) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Text(
                text = "Enviar mensaje",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
            )
            Text(
                text = "Este contacto no usa la app. Elige por donde chatear:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            OpcionMensajeria(
                icon = Icons.Filled.MailOutline,
                label = "SMS",
                onClick = {
                    onDismiss()
                    onSms(phone)
                }
            )
            OpcionMensajeria(
                icon = Icons.Filled.Call,
                label = "WhatsApp",
                onClick = {
                    onDismiss()
                    onWhatsApp(phone)
                }
            )
            OpcionMensajeria(
                icon = Icons.AutoMirrored.Filled.Send,
                label = "Telegram",
                onClick = {
                    onDismiss()
                    onTelegram(phone)
                }
            )
        }
    }
}

@Composable
private fun OpcionMensajeria(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/** Compone el texto plano que se envia al compartir un contacto. */
private fun textoParaCompartir(uiState: DetalleContactoUiState.Success): String = buildString {
    appendLine(uiState.fullName)
    if (uiState.company.isNotBlank()) appendLine(uiState.company)
    if (uiState.phone.isNotBlank()) appendLine("Telefono: ${uiState.phone}")
    if (uiState.email.isNotBlank()) appendLine("Correo: ${uiState.email}")
}.trim()

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

package com.yaeldev.cursitodefundamentosandroid.feature.chat.presentation.listaChats

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.yaeldev.cursitodefundamentosandroid.core.ui.components.AppBottomBar
import com.yaeldev.cursitodefundamentosandroid.core.ui.components.AppTab
import com.yaeldev.cursitodefundamentosandroid.core.ui.theme.AppTheme
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.models.Chat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaChatsScreen(
    modifier: Modifier = Modifier,
    uiState: ListaChatsUiState,
    actions: ListaChatsActions
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
                        text = "Chats",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.displayLarge
                    )
                }
            )
        },
        bottomBar = {
            AppBottomBar(
                seleccionada = AppTab.Chats,
                onContactos = actions.onNavigateToContactos,
                onFavoritos = actions.onNavigateToFavoritos,
                onPerfil = actions.onNavigateToPerfil
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
                ListaChatsUiState.Loading ->
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                ListaChatsUiState.Empty ->
                    Text(
                        text = "Aun no tienes conversaciones.\nAbre el detalle de un contacto y toca Mensaje.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp)
                    )

                is ListaChatsUiState.Error ->
                    Text(text = uiState.message, modifier = Modifier.align(Alignment.Center))

                is ListaChatsUiState.Success ->
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(uiState.chats) { chat ->
                            ChatRow(
                                chat = chat,
                                miUid = uiState.miUid,
                                onClick = {
                                    actions.onAbrirChat(
                                        chat.id,
                                        chat.otroNombre(uiState.miUid),
                                        chat.otroUid(uiState.miUid)
                                    )
                                }
                            )
                        }
                    }
            }
        }
    }
}

@Composable
private fun ChatRow(chat: Chat, miUid: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = chat.inicial(miUid),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.size(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = chat.otroNombre(miUid),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = chat.ultimoMensaje.ifBlank { "Sin mensajes aun" },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        if (chat.ultimoTimestamp > 0L) {
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = horaCorta(chat.ultimoTimestamp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun horaCorta(millis: Long): String =
    SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(millis))

@Preview(showBackground = true)
@Composable
private fun ListaChatsPreview(
    @PreviewParameter(ListaChatsPreviewParameterProvider::class) state: ListaChatsUiState
) {
    AppTheme {
        ListaChatsScreen(
            uiState = state,
            actions = ListaChatsActions()
        )
    }
}

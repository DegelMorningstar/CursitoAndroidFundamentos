package com.yaeldev.cursitodefundamentosandroid.feature.chat.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.yaeldev.cursitodefundamentosandroid.core.ui.theme.AppTheme
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.models.Mensaje

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    uiState: ChatUiState,
    actions: ChatActions
) {
    val listState = rememberLazyListState()

    // Auto-scroll al ultimo mensaje cuando llega uno nuevo.
    LaunchedEffect(uiState.mensajes.size) {
        if (uiState.mensajes.isNotEmpty()) {
            listState.animateScrollToItem(uiState.mensajes.lastIndex)
        }
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
                    // Tocar el nombre abre el detalle de perfil del otro usuario.
                    Text(
                        text = uiState.titulo,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.clickable(onClick = actions.onVerPerfil)
                    )
                }
            )
        },
        bottomBar = {
            BarraEntrada(
                texto = uiState.texto,
                onTextoChange = actions.onTextoChange,
                onEnviar = actions.onEnviar
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            when {
                uiState.cargando ->
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                uiState.error != null ->
                    Text(text = uiState.error, modifier = Modifier.align(Alignment.Center))

                uiState.mensajes.isEmpty() ->
                    Text(
                        text = "Envia el primer mensaje.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.Center)
                    )

                else ->
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(uiState.mensajes, key = { it.id }) { mensaje ->
                            Burbuja(mensaje = mensaje, esMio = mensaje.autor == uiState.miUid)
                        }
                    }
            }
        }
    }
}

@Composable
private fun Burbuja(mensaje: Mensaje, esMio: Boolean) {
    val alineacion = if (esMio) Alignment.End else Alignment.Start
    val colorFondo =
        if (esMio) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val colorTexto =
        if (esMio) Color.White else MaterialTheme.colorScheme.onSurface

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = alineacion) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = colorFondo,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                text = mensaje.texto,
                style = MaterialTheme.typography.bodyLarge,
                color = colorTexto,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
        // Estado entregado/leido solo en MIS mensajes.
        if (esMio) {
            Text(
                text = if (mensaje.leido) "Leido ✓✓" else "Entregado ✓",
                style = MaterialTheme.typography.labelSmall,
                color = if (mensaje.leido) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp, end = 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BarraEntrada(
    texto: String,
    onTextoChange: (String) -> Unit,
    onEnviar: () -> Unit
) {
    Surface(color = MaterialTheme.colorScheme.surface) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = texto,
                onValueChange = onTextoChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Mensaje") },
                maxLines = 4,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(24.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            IconButton(
                onClick = onEnviar,
                enabled = texto.isNotBlank()
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Enviar",
                    tint = if (texto.isNotBlank()) MaterialTheme.colorScheme.primary
                           else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatPreview(
    @PreviewParameter(ChatPreviewParameterProvider::class) state: ChatUiState
) {
    AppTheme {
        ChatScreen(
            uiState = state,
            actions = ChatActions()
        )
    }
}

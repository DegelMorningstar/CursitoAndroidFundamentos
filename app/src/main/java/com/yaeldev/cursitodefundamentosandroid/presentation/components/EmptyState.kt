package com.yaeldev.cursitodefundamentosandroid.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yaeldev.cursitodefundamentosandroid.presentation.theme.AppTheme

@Composable
fun EmptyState(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    body: String
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = body,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun EmptyContactos(modifier: Modifier = Modifier) {
    EmptyState(
        modifier = modifier,
        icon = Icons.Outlined.Person,
        title = "Sin contactos",
        body = "Agrega tu primer contacto con el boton +"
    )
}

@Composable
fun EmptyBusqueda(modifier: Modifier = Modifier, query: String = "") {
    EmptyState(
        modifier = modifier,
        icon = Icons.Outlined.Search,
        title = "Sin resultados",
        body = "No se encontraron contactos para \"$query\""
    )
}

@Composable
fun EmptyFavoritos(modifier: Modifier = Modifier) {
    EmptyState(
        modifier = modifier,
        icon = Icons.Outlined.Star,
        title = "Sin favoritos",
        body = "Marca contactos con la estrella para verlos aqui"
    )
}

@Preview(showBackground = true)
@Composable
private fun EmptyStatePreview() {
    AppTheme {
        EmptyContactos()
    }
}

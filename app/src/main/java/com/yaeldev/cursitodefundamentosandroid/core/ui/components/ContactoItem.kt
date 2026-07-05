package com.yaeldev.cursitodefundamentosandroid.core.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yaeldev.cursitodefundamentosandroid.core.ui.theme.AppTheme
import com.yaeldev.cursitodefundamentosandroid.core.ui.theme.colorAvatar
import com.yaeldev.cursitodefundamentosandroid.core.ui.theme.pink

@Composable
fun ContactoItem(
    modifier: Modifier = Modifier,
    fullName: String,
    cellPhone: String,
    esFavorito: Boolean,
    onClick: () -> Unit,
    onToggleFavorito: () -> Unit
) {
    Column(
        modifier = modifier
            .height(70.dp)
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
    ) {
        ContactDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(
                initials = fullName.first().toString(),
                color = colorAvatar(fullName)
            )
            ContactInfo(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .weight(1f),
                fullName = fullName,
                cellPhone = cellPhone
            )
            BotonFavorito(
                esFavorito = esFavorito,
                onClick = onToggleFavorito
            )
        }
        ContactDivider()
    }
}

@Composable
fun ContactDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(modifier = modifier, thickness = 1.dp, color = MaterialTheme.colorScheme.outline)
}

@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    initials: String,
    color: Color = pink
) {
    Box(
        modifier = modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            color = Color.White,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

@Composable
fun ContactInfo(
    modifier: Modifier = Modifier,
    fullName: String,
    cellPhone: String
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = fullName,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.padding(top = 4.dp))
        Text(
            text = cellPhone,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun BotonFavorito(
    modifier: Modifier = Modifier,
    esFavorito: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        //TODO: Cambiar icono
        Icon(
            imageVector = Icons.Outlined.Star,
            contentDescription = null,
            tint = if(esFavorito) Color(0xFFf59f00) else Color(0xFFd1d5db)
        )
    }
}


@Preview(name = "Default", showBackground = true)
@Preview(name = "dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "LTR", locale = "en")
@Preview(name = "large font", fontScale = 1.5f)
@Composable
fun ContactoItemPreview() {
    AppTheme {
        ContactoItem(
            fullName = "Yael Montes Camacho",
            cellPhone = "+52 7771234568",
            esFavorito = true,
            onClick = {},
            onToggleFavorito = {}
        )
    }
}
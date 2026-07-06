package com.yaeldev.cursitodefundamentosandroid.feature.onboarding.presentation.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yaeldev.cursitodefundamentosandroid.core.ui.theme.AppTheme
import com.yaeldev.cursitodefundamentosandroid.core.ui.theme.PlusJakartaSansFamily
import com.yaeldev.cursitodefundamentosandroid.core.ui.theme.primary

private val PuntoInactivo = Color(0xFFE2E5F7)

/**
 * Stateless: pinta el onboarding (ilustracion + texto + puntos + boton). Toda la
 * logica de paso/persistencia vive en el ViewModel; aqui solo se renderiza el estado.
 */
@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    uiState: OnboardingUiState,
    actions: OnboardingActions
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Omitir (arriba a la derecha)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, end = 12.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = actions.onOmitir) {
                Text(
                    text = "Omitir",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = PlusJakartaSansFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }

        // Ilustracion + texto (con transicion entre paginas)
        AnimatedContent(
            targetState = uiState.paso,
            transitionSpec = {
                (slideInHorizontally { it / 3 } + fadeIn(tween(250))) togetherWith
                    (slideOutHorizontally { -it / 3 } + fadeOut(tween(250)))
            },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            label = "onboarding-pagina"
        ) { paso ->
            ContenidoPagina(uiState.paginas[paso])
        }

        // Puntos + boton
        Column(
            modifier = Modifier.padding(start = 32.dp, end = 32.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            IndicadorPuntos(total = uiState.paginas.size, activo = uiState.paso, accent = uiState.paginaActual.accent)
            Button(
                onClick = actions.onSiguiente,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primary)
            ) {
                Text(
                    text = uiState.etiquetaBoton,
                    color = Color.White,
                    fontFamily = PlusJakartaSansFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
private fun ContenidoPagina(pagina: OnboardingPagina) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(168.dp)
                .clip(CircleShape)
                .background(pagina.accent.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            val icono = when (pagina.icono) {
                OnboardingIcono.Contactos -> Icons.Filled.Person
                OnboardingIcono.Mensaje -> Icons.AutoMirrored.Filled.Send
            }
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = pagina.accent,
                modifier = Modifier.size(76.dp)
            )
        }
        Spacer(Modifier.height(28.dp))
        Text(
            text = pagina.titulo,
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = PlusJakartaSansFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = pagina.cuerpo,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontFamily = PlusJakartaSansFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 24.sp,
            textAlign = TextAlign.Center
        )
    }
}

/** Fila de puntos; el activo se ensancha y toma el color de acento de la pagina. */
@Composable
private fun IndicadorPuntos(total: Int, activo: Int, accent: Color) {
    Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
        repeat(total) { indice ->
            val esActivo = indice == activo
            val ancho by animateDpAsState(
                targetValue = if (esActivo) 22.dp else 7.dp,
                animationSpec = tween(200),
                label = "punto$indice"
            )
            Box(
                Modifier
                    .width(ancho)
                    .height(7.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (esActivo) accent else PuntoInactivo)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingScreenPreview(
    @PreviewParameter(OnboardingPreviewParameterProvider::class) state: OnboardingUiState
) {
    AppTheme {
        OnboardingScreen(uiState = state, actions = OnboardingActions())
    }
}

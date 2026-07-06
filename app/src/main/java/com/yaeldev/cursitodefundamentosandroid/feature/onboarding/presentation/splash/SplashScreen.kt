package com.yaeldev.cursitodefundamentosandroid.feature.onboarding.presentation.splash

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yaeldev.cursitodefundamentosandroid.core.ui.theme.AppTheme
import com.yaeldev.cursitodefundamentosandroid.core.ui.theme.PlusJakartaSansFamily
import com.yaeldev.cursitodefundamentosandroid.core.ui.theme.primary

private val SplashBlueTop = Color(0xFF4361EE)
private val SplashBlueBottom = Color(0xFF3949AB)
private val LogoBar = Color(0xFFC5CAE9)
private val BadgeGreen = Color(0xFF00C853)

/**
 * Pantalla de bienvenida (branding) que se muestra al arrancar la app mientras se
 * decide el destino. Stateless: solo pinta el logo, el nombre y unos puntos animados.
 */
@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(listOf(SplashBlueTop, SplashBlueBottom))
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            LogoContactos()
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Contactos Work",
                    color = Color.White,
                    fontFamily = PlusJakartaSansFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Tu agenda profesional, siempre a la mano",
                    color = Color.White.copy(alpha = 0.75f),
                    fontFamily = PlusJakartaSansFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                )
            }
        }

        PuntosCargando(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 56.dp)
        )
    }
}

/** Cuadro blanco con la marca (punto + barra) y un badge verde de "check". */
@Composable
private fun LogoContactos() {
    Box(
        modifier = Modifier
            .size(96.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(Color.White.copy(alpha = 0.14f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Box(
                    Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(primary)
                )
                Box(
                    Modifier
                        .width(34.dp)
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(LogoBar)
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 6.dp, y = 6.dp)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(BadgeGreen)
                    .border(3.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(11.dp)
                )
            }
        }
    }
}

/** Tres puntos con un pulso de opacidad desfasado. */
@Composable
private fun PuntosCargando(modifier: Modifier = Modifier) {
    val transicion = rememberInfiniteTransition(label = "puntos")
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        repeat(3) { indice ->
            val alfa by transicion.animateFloat(
                initialValue = 0.4f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(700, delayMillis = indice * 200),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "punto$indice"
            )
            Box(
                Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = alfa))
            )
        }
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    AppTheme {
        SplashScreen()
    }
}

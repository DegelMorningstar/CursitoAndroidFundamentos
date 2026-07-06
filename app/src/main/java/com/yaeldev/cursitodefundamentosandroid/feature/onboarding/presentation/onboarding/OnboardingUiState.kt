package com.yaeldev.cursitodefundamentosandroid.feature.onboarding.presentation.onboarding

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

/** Ilustracion de cada pagina del onboarding. */
enum class OnboardingIcono { Contactos, Mensaje }

/** Contenido de una pagina del onboarding (texto + ilustracion + acento). */
data class OnboardingPagina(
    val titulo: String,
    val cuerpo: String,
    val accent: Color,
    val icono: OnboardingIcono
)

/** Paginas del onboarding, en orden (contenido de la spec de diseno). */
val paginasOnboarding: List<OnboardingPagina> = listOf(
    OnboardingPagina(
        titulo = "Maneja tus contactos",
        cuerpo = "Guarda, organiza y encuentra a tus colegas y clientes en segundos, todo en un solo lugar.",
        accent = Color(0xFF4361EE),
        icono = OnboardingIcono.Contactos
    ),
    OnboardingPagina(
        titulo = "Manda mensajes al instante",
        cuerpo = "Llama, escribe o comparte informacion de contacto sin salir de la app.",
        accent = Color(0xFF00897B),
        icono = OnboardingIcono.Mensaje
    )
)

/**
 * Pantalla con input (paso a paso), por eso es **data class** y no sealed interface.
 * Las paginas son estaticas; solo cambia [paso]. Lo derivado se calcula aqui.
 */
data class OnboardingUiState(
    val paso: Int = 0,
    val paginas: List<OnboardingPagina> = paginasOnboarding
) {
    val paginaActual: OnboardingPagina get() = paginas[paso]
    val esUltima: Boolean get() = paso == paginas.lastIndex
    val etiquetaBoton: String get() = if (esUltima) "Comenzar" else "Siguiente"
}

data class OnboardingActions(
    val onSiguiente: () -> Unit = {},
    val onOmitir: () -> Unit = {}
)

class OnboardingPreviewParameterProvider : PreviewParameterProvider<OnboardingUiState> {
    override val values: Sequence<OnboardingUiState>
        get() = sequenceOf(
            OnboardingUiState(paso = 0),
            OnboardingUiState(paso = 1)
        )
}

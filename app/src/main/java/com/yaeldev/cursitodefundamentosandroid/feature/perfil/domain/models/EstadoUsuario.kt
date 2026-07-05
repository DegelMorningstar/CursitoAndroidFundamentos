package com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.models

/** Estado de disponibilidad del usuario. Se persiste por su [name] en Firestore. */
enum class EstadoUsuario(val etiqueta: String) {
    Disponible("Disponible"),
    Ocupado("Ocupado"),
    EnVacaciones("En vacaciones");

    companion object {
        /** Mapea el valor guardado a un enum; default Disponible si no coincide. */
        fun desde(valor: String?): EstadoUsuario =
            entries.firstOrNull { it.name == valor } ?: Disponible
    }
}
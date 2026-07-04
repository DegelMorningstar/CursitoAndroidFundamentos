package com.yaeldev.cursitodefundamentosandroid.navigation

import kotlinx.serialization.Serializable

@Serializable
object ListaContacto

// Las rutas de detalle y edicion solo llevan el id del contacto: el dato
// completo se resolvera desde el repositorio (hoy datos de muestra, manana API).
@Serializable
data class EditaContacto(val id: String)

@Serializable
object AgregaContacto

@Serializable
data class DetalleContacto(val id: String)

@Serializable
object Favoritos
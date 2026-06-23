package com.yaeldev.cursitodefundamentosandroid.navigation

import com.yaeldev.cursitodefundamentosandroid.models.Contacto
import kotlinx.serialization.Serializable

@Serializable
object ListaContacto

@Serializable
object EditaContacto //TODO: FALTAN PARAMETROS

@Serializable
object AgregaContacto

@Serializable
data class DetalleContacto(
    val nombre:String,
    val telefono:String,
    val apodo:String = "",
    val empresa:String = "",
    val puesto:String = ""
)

@Serializable
object Favoritos
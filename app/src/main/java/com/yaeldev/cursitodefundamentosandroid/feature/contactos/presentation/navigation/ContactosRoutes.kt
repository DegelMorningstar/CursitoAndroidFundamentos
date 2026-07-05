package com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object ListaContacto

@Serializable
data class EditaContacto(val id: String)

@Serializable
object AgregaContacto

@Serializable
data class DetalleContacto(val id: String)

@Serializable
object Favoritos

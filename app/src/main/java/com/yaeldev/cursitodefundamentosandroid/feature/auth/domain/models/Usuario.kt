package com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.models

import com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.models.EstadoUsuario

/**
 * Usuario autenticado. [id]/[email]/[nombre] vienen de FirebaseAuth; [foto]
 * (base64 de un thumbnail) y [estado] se guardan en el doc `usuarios/{uid}`.
 */
data class Usuario(
    val id: String = "",
    val email: String = "",
    val nombre: String = "",
    val foto: String = "",
    val estado: EstadoUsuario = EstadoUsuario.Disponible
)

package com.yaeldev.cursitodefundamentosandroid.feature.perfil.data.remote.mappers

import com.google.firebase.firestore.DocumentSnapshot
import com.yaeldev.cursitodefundamentosandroid.core.util.Catalogo.CAMPO_EMAIL
import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.models.Usuario
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.models.EstadoUsuario

fun DocumentSnapshot.toUsuarioDirectorio(): Usuario = Usuario(
    id = getString("uid") ?: id,
    email = getString(CAMPO_EMAIL).orEmpty(),
    nombre = getString("nombre").orEmpty(),
    foto = getString("foto").orEmpty(),
    estado = EstadoUsuario.desde(getString("estado"))
)
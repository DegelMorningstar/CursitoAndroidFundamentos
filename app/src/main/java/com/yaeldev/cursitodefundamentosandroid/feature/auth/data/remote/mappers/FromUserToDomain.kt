package com.yaeldev.cursitodefundamentosandroid.feature.auth.data.remote.mappers

import com.google.firebase.auth.FirebaseUser
import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.models.Usuario

fun FirebaseUser.toUsuario(): Usuario = Usuario(
    id = uid,
    email = email.orEmpty(),
    nombre = displayName.orEmpty()
)
package com.yaeldev.cursitodefundamentosandroid.util

import com.google.firebase.firestore.FirebaseFirestoreException
import java.io.IOException
import java.util.concurrent.TimeoutException

fun Throwable.toMessage(): String = when (this) {
    is FirebaseFirestoreException -> when (code) {
        FirebaseFirestoreException.Code.UNAVAILABLE ->
            "Sin conexion con el servidor. Revisa tu internet."
        FirebaseFirestoreException.Code.PERMISSION_DENIED ->
            "No tienes permisos para realizar esta operacion."
        FirebaseFirestoreException.Code.NOT_FOUND ->
            "El contacto no existe."
        FirebaseFirestoreException.Code.DEADLINE_EXCEEDED ->
            "El tiempo de espera se agoto, revisa tu conexion."
        else -> "Error de Firestore. Intentelo de nuevo."
    }
    is IOException -> "Sin conexion, revisa tu internet."
    is TimeoutException -> "El tiempo de espera se agoto, revisa tu conexion."
    else -> "Algo salio mal. Intentelo de nuevo."
}

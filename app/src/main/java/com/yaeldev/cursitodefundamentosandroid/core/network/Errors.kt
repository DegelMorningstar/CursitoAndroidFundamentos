package com.yaeldev.cursitodefundamentosandroid.core.network

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestoreException
import java.io.IOException
import java.util.concurrent.TimeoutException

fun Throwable.toMessage(): String = when (this) {
    is FirebaseAuthWeakPasswordException ->
        "La contraseña es muy debil, usa al menos 6 caracteres."
    is FirebaseAuthUserCollisionException ->
        "Ese correo ya esta registrado."
    is FirebaseAuthInvalidUserException ->
        "No existe una cuenta con ese correo."
    is FirebaseAuthInvalidCredentialsException ->
        "Correo o contraseña incorrectos."
    is FirebaseNetworkException ->
        "Sin conexion, revisa tu internet."
    // Cualquier otro fallo de Auth: mapeamos por errorCode; incluye el caso mas
    // comun al empezar (proveedor Email/Password deshabilitado en la consola).
    is FirebaseAuthException -> when (errorCode) {
        "ERROR_OPERATION_NOT_ALLOWED" ->
            "El inicio de sesion con correo no esta habilitado en Firebase."
        "ERROR_INVALID_EMAIL" ->
            "El correo no tiene un formato valido."
        "ERROR_EMAIL_ALREADY_IN_USE" ->
            "Ese correo ya esta registrado."
        "ERROR_USER_DISABLED" ->
            "Esta cuenta ha sido deshabilitada."
        else -> "Error de autenticacion ($errorCode)."
    }
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

package com.yaeldev.cursitodefundamentosandroid.util

import com.google.gson.stream.MalformedJsonException
import okio.IOException
import retrofit2.HttpException
import java.util.concurrent.TimeoutException

fun Throwable.toMessage() : String = when(this) {
    is MalformedJsonException -> "Ocurrio un error al leer la respueta. Intentelo de nuevo."
    is IOException -> "Sin conexion, revisa tu internet."
    is TimeoutException -> "El tiempo de espera se agoto, reivse su conexion."
    is HttpException -> "Error del servidor (${code()})"
    else -> "Algo salio mal. Intentelo de nuevo."
}
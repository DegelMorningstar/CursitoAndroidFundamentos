package com.yaeldev.cursitodefundamentosandroid.core.network

import kotlin.coroutines.cancellation.CancellationException

/**
 * Ejecuta [bloque] envolviendo el resultado en Result. Re-lanza
 * CancellationException; cualquier otro error se traduce a Result.Error.
 */
inline fun <T> ejecutar(bloque: () -> T): Result<T> {
    return try {
        Result.Success(bloque())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.Error(e.toMessage())
    }
}
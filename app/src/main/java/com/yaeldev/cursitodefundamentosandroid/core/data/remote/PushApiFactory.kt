package com.yaeldev.cursitodefundamentosandroid.core.data.remote

import com.yaeldev.cursitodefundamentosandroid.core.util.Catalogo.WORKER_PUSH_URL
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object PushApiFactory {
    fun create(): PushApi {
        val json = Json { ignoreUnknownKeys = true }
        val contentType = "application/json".toMediaType()
        // La URL del Worker es config y va en el baseUrl (debe terminar en "/").
        // Si algún día queda vacía, un placeholder .invalid evita crashear al construir;
        // NotificadorPushWorker de todos modos no envía cuando la URL está vacía.
        val base = WORKER_PUSH_URL.trim().let {
            when {
                it.isBlank() -> "https://push.invalid/"
                it.endsWith("/") -> it
                else -> "$it/"
            }
        }
        return Retrofit.Builder()
            .baseUrl(base)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(PushApi::class.java)
    }
}
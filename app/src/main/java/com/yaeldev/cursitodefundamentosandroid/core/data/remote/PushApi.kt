package com.yaeldev.cursitodefundamentosandroid.core.data.remote

import com.yaeldev.cursitodefundamentosandroid.core.data.remote.dto.PushRequest
import com.yaeldev.cursitodefundamentosandroid.core.util.Catalogo.WORKER_PUSH_URL
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

/** Cliente Retrofit del Worker de push (genérico, reutilizable por cualquier feature). */
interface PushApi {
    // "." resuelve a la baseUrl (la del Worker); el Worker responde en la raíz.
    @POST(".")
    suspend fun notificar(@Body body: PushRequest): Response<ResponseBody>
}

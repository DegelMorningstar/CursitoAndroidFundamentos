package com.yaeldev.cursitodefundamentosandroid.data.remote

import com.yaeldev.cursitodefundamentosandroid.data.remote.dto.user.UserResponseDTO
import retrofit2.http.GET

interface RandomUserAPI {

    @GET("?results=1")
    suspend fun obtenerContactos(): UserResponseDTO

}
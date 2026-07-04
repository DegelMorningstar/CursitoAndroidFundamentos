package com.yaeldev.cursitodefundamentosandroid.data.remote

import com.yaeldev.cursitodefundamentosandroid.data.remote.dto.user.UserDTO
import com.yaeldev.cursitodefundamentosandroid.data.remote.dto.user.UserResponseDTO
import com.yaeldev.cursitodefundamentosandroid.domain.models.Contacto
import retrofit2.http.GET
import retrofit2.http.Path

interface RandomUserAPI {

    @GET("?results=1")
    suspend fun obtenerContactos(): UserResponseDTO

    @GET("contacto/{id}")
    suspend fun obtenerContactoById(@Path("id") id:String): UserDTO?

}
package com.yaeldev.cursitodefundamentosandroid.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue

object AppService {

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://randomuser.me/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp)
            .build()
    }

    private val okHttp by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val randomUserApi: RandomUserAPI = getRetrofit().create(RandomUserAPI::class.java)

}
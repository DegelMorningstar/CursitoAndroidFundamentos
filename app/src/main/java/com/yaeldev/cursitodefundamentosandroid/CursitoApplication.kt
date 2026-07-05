package com.yaeldev.cursitodefundamentosandroid

import android.app.Application
import com.yaeldev.cursitodefundamentosandroid.core.di.AppContainer
import com.yaeldev.cursitodefundamentosandroid.core.di.DefaultAppContainer

/**
 * Application dueña del [AppContainer] durante todo el ciclo de vida del proceso.
 * Solo expone el container; nada de logica de negocio aqui.
 */
class CursitoApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}

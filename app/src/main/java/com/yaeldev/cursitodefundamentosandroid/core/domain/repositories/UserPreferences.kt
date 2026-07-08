package com.yaeldev.cursitodefundamentosandroid.core.domain.repositories

interface UserPreferences {
    fun guardarTema(isDarkMode: Boolean)
    fun leerTema(): Boolean
}
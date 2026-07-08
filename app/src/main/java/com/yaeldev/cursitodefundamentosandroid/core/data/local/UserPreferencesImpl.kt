package com.yaeldev.cursitodefundamentosandroid.core.data.local

import android.content.Context
import androidx.core.content.edit
import com.yaeldev.cursitodefundamentosandroid.core.domain.repositories.UserPreferences
import com.yaeldev.cursitodefundamentosandroid.core.util.Catalogo.SHARED_PREFERENCES_USER

class UserPreferencesImpl(
    context: Context
) : UserPreferences {

    private val prefs = context.getSharedPreferences(SHARED_PREFERENCES_USER,Context.MODE_PRIVATE)

    override fun guardarTema(isDarkMode: Boolean) {
        prefs.edit{ putBoolean(TEMA_KEY,isDarkMode) }
    }

    override fun leerTema(): Boolean {
        return prefs.getBoolean(TEMA_KEY,false)
    }

    companion object {
        const val TEMA_KEY = "user_preference_dark_mode"
    }

}
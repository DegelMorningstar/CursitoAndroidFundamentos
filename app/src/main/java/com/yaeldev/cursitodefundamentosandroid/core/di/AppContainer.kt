package com.yaeldev.cursitodefundamentosandroid.core.di

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yaeldev.cursitodefundamentosandroid.CursitoApplication
import com.yaeldev.cursitodefundamentosandroid.core.data.local.UserPreferencesImpl
import com.yaeldev.cursitodefundamentosandroid.core.data.remote.NotificadorRepositoryImpl
import com.yaeldev.cursitodefundamentosandroid.core.domain.repositories.NotificadorRepository
import com.yaeldev.cursitodefundamentosandroid.core.domain.repositories.UserPreferences
import com.yaeldev.cursitodefundamentosandroid.feature.auth.data.remote.firebase.AuthRepositoryFirebase
import com.yaeldev.cursitodefundamentosandroid.feature.auth.domain.repositories.AuthRepository
import com.yaeldev.cursitodefundamentosandroid.feature.chat.data.remote.firestore.ChatRepositoryFirestore
import com.yaeldev.cursitodefundamentosandroid.feature.chat.data.remote.push.NotificadorMensajesChat
import com.yaeldev.cursitodefundamentosandroid.feature.chat.domain.repositories.ChatRepository
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.data.remote.firestore.ContactoRepositoryFirestore
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.domain.repositories.ContactoRepository
import com.yaeldev.cursitodefundamentosandroid.feature.onboarding.data.local.OnboardingPreferencesSharedPrefs
import com.yaeldev.cursitodefundamentosandroid.feature.onboarding.domain.repositories.OnboardingPreferences
import com.yaeldev.cursitodefundamentosandroid.feature.onboarding.domain.usecases.MarcarOnboardingCompletadoUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.onboarding.domain.usecases.OnboardingCompletadoUseCase
import com.yaeldev.cursitodefundamentosandroid.feature.onboarding.presentation.onboarding.OnboardingViewModel
import com.yaeldev.cursitodefundamentosandroid.feature.onboarding.presentation.splash.SplashViewModel
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.data.remote.firebase.PerfilRepositoryFirebase
import com.yaeldev.cursitodefundamentosandroid.feature.perfil.domain.repositories.PerfilRepository

/**
 * Composition root de DI manual. Crea las **instancias unicas** de cada repositorio
 * (by lazy) y ensambla un contenedor por feature, pasando los repos compartidos que
 * cada uno necesita. Vive en core/di y es el UNICO lugar donde se conocen los
 * features (por eso aqui si se permite core -> feature; ver CLAUDE.md).
 */
interface AppContainer {
    val auth: AuthContainer
    val contactos: ContactosContainer
    val chat: ChatContainer
    val perfil: PerfilContainer
    val onboarding: OnboardingContainer
}

class DefaultAppContainer(
    // Necesario para preferencias locales (SharedPreferences del onboarding).
    private val context: Context
) : AppContainer {

    // Servicio de push genérico, único para toda la app (reutilizable por cualquier feature).
    private val notificadorRepository: NotificadorRepository by lazy { NotificadorRepositoryImpl() }

    // Una sola instancia de cada repositorio para toda la app.
    private val contactoRepository: ContactoRepository by lazy { ContactoRepositoryFirestore() }
    private val authRepository: AuthRepository by lazy { AuthRepositoryFirebase() }
    private val chatRepository: ChatRepository by lazy {
        ChatRepositoryFirestore(notificadorChat = NotificadorMensajesChat(notificadorRepository))
    }
    private val perfilRepository: PerfilRepository by lazy { PerfilRepositoryFirebase() }
    private val onboardingPreferences: OnboardingPreferences by lazy {
        OnboardingPreferencesSharedPrefs(context)
    }
    private val userPreferences: UserPreferences by lazy {
        UserPreferencesImpl(context)
    }

    override val auth: AuthContainer by lazy { AuthContainer(authRepository) }
    override val contactos: ContactosContainer by lazy {
        ContactosContainer(contactoRepository, perfilRepository, chatRepository)
    }
    override val chat: ChatContainer by lazy { ChatContainer(chatRepository) }
    override val perfil: PerfilContainer by lazy { PerfilContainer(perfilRepository, authRepository,userPreferences) }
    override val onboarding: OnboardingContainer by lazy { OnboardingContainer(onboardingPreferences) }

}

/** Acceso al container desde Compose (dueño: el Application). */
@Composable
fun appContainer(): AppContainer =
    (LocalContext.current.applicationContext as CursitoApplication).container

package com.yaeldev.cursitodefundamentosandroid

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.yaeldev.cursitodefundamentosandroid.core.notifications.crearCanalMensajes
import com.yaeldev.cursitodefundamentosandroid.core.notifications.registrarTokenFcm
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.yaeldev.cursitodefundamentosandroid.core.ui.components.AppSnackbar
import com.yaeldev.cursitodefundamentosandroid.core.ui.theme.AppTheme
import com.yaeldev.cursitodefundamentosandroid.feature.auth.presentation.navigation.Login
import com.yaeldev.cursitodefundamentosandroid.feature.contactos.presentation.navigation.ListaContacto
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    // Registra el token FCM cuando hay sesión (login o arranque con sesión activa).
    private val authListener = FirebaseAuth.AuthStateListener { auth ->
        if (auth.currentUser != null) registrarTokenFcm()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        crearCanalMensajes(this)
        FirebaseAuth.getInstance().addAuthStateListener(authListener)
        val destinoInicial: Any =
            if (FirebaseAuth.getInstance().currentUser != null) ListaContacto else Login
        setContent {
            // Preferencia de tema en memoria (aun no se persiste).
            var temaOscuro by remember { mutableStateOf(false) }
            val navController = rememberNavController()
            AppTheme(darkTheme = temaOscuro) {
                PedirPermisoNotificaciones()
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()
                Box(modifier = Modifier.fillMaxSize()) {
                    AppHost(
                        modifier = Modifier.statusBarsPadding(),
                        navController = navController,
                        startDestination = destinoInicial,
                        temaOscuro = temaOscuro,
                        onToggleTema = { temaOscuro = it },
                        mostrarMensaje = { mensaje ->
                            scope.launch { snackbarHostState.showSnackbar(mensaje) }
                        }
                    )
                    SnackbarHost(
                        hostState = snackbarHostState,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .navigationBarsPadding()
                    ) { data ->
                        AppSnackbar(message = data.visuals.message)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        FirebaseAuth.getInstance().removeAuthStateListener(authListener)
        super.onDestroy()
    }
}

/** Pide el permiso POST_NOTIFICATIONS en Android 13+ (no-op en versiones previas). */
@Composable
private fun PedirPermisoNotificaciones() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }
    LaunchedEffect(Unit) {
        val concedido = ContextCompat.checkSelfPermission(
            context, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        if (!concedido) launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}

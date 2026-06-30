package com.yaeldev.cursitodefundamentosandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.yaeldev.cursitodefundamentosandroid.navigation.AppHost
import com.yaeldev.cursitodefundamentosandroid.presentation.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            AppTheme {
                AppHost(
                    modifier = Modifier.statusBarsPadding(),
                    navController = navController
                )
            }
        }
    }
}

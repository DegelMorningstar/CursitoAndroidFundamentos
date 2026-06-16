package com.yaeldev.cursitodefundamentosandroid.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yaeldev.cursitodefundamentosandroid.ui.theme.CursitoDeFundamentosAndroidTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaContactoScreen(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = "Contactos",
                        fontWeight = FontWeight.Bold,
                        color = Color.Blue
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(
                            Icons.Filled.AccountCircle,
                            contentDescription = null
                        )
                    },
                    label = { Text("Contactos") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null
                        )
                    },
                    label = { Text("Favoritos") }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.Red)
                .padding(16.dp)
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)),
                value = "",
                onValueChange = {}
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 12.dp)
                    .background(Color.Green)
            ) {
                item {
                    Column() {
                        HorizontalDivider(thickness = 2.dp)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Yellow)
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(Color.Cyan),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("YM")
                            }
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 12.dp)
                                    .weight(1f)
                            ) {
                                Text("Yael Montes Camacho")
                                Text("+52 5512345678")
                            }
                            IconButton({}) {
                                Icon(Icons.Default.Star,null)
                            }
                        }
                        HorizontalDivider(thickness = 2.dp)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ListaContactoPreview() {
    CursitoDeFundamentosAndroidTheme {
        Column {
            ListaContactoScreen()
        }
    }
}
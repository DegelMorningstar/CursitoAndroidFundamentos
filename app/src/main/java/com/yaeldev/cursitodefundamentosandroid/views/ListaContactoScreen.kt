package com.yaeldev.cursitodefundamentosandroid.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yaeldev.cursitodefundamentosandroid.models.Contacto
import com.yaeldev.cursitodefundamentosandroid.models.ContactoPersonal
import com.yaeldev.cursitodefundamentosandroid.models.ContactoTrabajo
import com.yaeldev.cursitodefundamentosandroid.ui.theme.AppTheme
import com.yaeldev.cursitodefundamentosandroid.views.components.ContactoItem
import com.yaeldev.cursitodefundamentosandroid.views.components.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaContactoScreen(
    modifier: Modifier = Modifier,
    onNavigateToAddContact: () -> Unit,
    onNavigateToDetail: (Contacto) -> Unit
) {

    val query = remember { mutableStateOf("") }
    var contactos: List<Contacto> by remember { mutableStateOf(listOf(
        ContactoPersonal(
            nombre = "Juan Yael Montes Camacho",
            telefono = "7771897745",
            apodo = "El patriota"
        ),
        ContactoTrabajo(
            nombre = "Ruben Estrada Zavala",
            telefono = "5512345678",
            empresa = "AsTecI",
            puesto = "El patron"
        ),
        ContactoPersonal(
            nombre = "Tulio Treviño",
            telefono = "7771897745",
            apodo = "El patriota"
        )
    )) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = "Contactos",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.displayLarge
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    colors = navBarItemColors(),
                    icon = {
                        Icon(
                            Icons.Filled.AccountCircle,
                            contentDescription = null
                        )
                    },
                    label = { Text("Contactos") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    colors = navBarItemColors(),
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
                onClick = onNavigateToAddContact,
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar contacto")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            SearchBar(
                query = query.value,
                onQueryChange = { nuevoValor ->
                    query.value = nuevoValor
                },
                onClear = {
                    query.value = ""
                }
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 12.dp)
            ) {
                val contactosFiltrados = if(query.value.isNotEmpty()) {
                    contactos.filter { it.nombre.contains(query.value, ignoreCase = true) }
                } else {
                    contactos
                }
                items(contactosFiltrados) { contacto ->
                    ContactoItem(
                        fullName = contacto.nombre,
                        cellPhone = contacto.telefono,
                        esFavorito = true,
                        onClick = {
                            onNavigateToDetail.invoke(contacto)
                        }
                    )
                }
            }
        }
    }
}

/**
 * §3.5 Bottom Navigation — pill activo primaryContainer, icono/label activo
 * primary, inactivo onSurfaceVariant. Necesario porque el esquema no define
 * secondaryContainer (el indicador por defecto saldria con un color ajeno).
 */
@Composable
internal fun navBarItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = MaterialTheme.colorScheme.primary,
    selectedTextColor = MaterialTheme.colorScheme.primary,
    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
)

@Preview
@Composable
private fun ListaContactoPreview() {
    AppTheme {
        Column {
            ListaContactoScreen(
                onNavigateToAddContact = {},
                onNavigateToDetail = {}
            )
        }
    }
}
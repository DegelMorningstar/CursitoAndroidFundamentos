package com.yaeldev.cursitodefundamentosandroid.data.local

import com.yaeldev.cursitodefundamentosandroid.domain.models.Contacto

object ContactosMuestra {

    val lista: List<Contacto> = listOf(
        Contacto(
            id = 1,
            first = "Juan Yael",
            last = "Montes Camacho",
            phone = "7771897745",
            company = "YaelDev",
            favorite = true
        ),
        Contacto(
            id = 2,
            first = "Ruben",
            last = "Estrada Zavala",
            phone = "5512345678",
            email = "ruben@asteci.com",
            company = "AsTecI"
        ),
        Contacto(id = 3, first = "Tulio", last = "Treviño", phone = "7773344551", favorite = true),
        Contacto(
            id = 4,
            first = "Ana",
            last = "Lopez",
            phone = "5523456789",
            email = "ana.lopez@correo.com"
        ),
        Contacto(
            id = 5,
            first = "Luis",
            last = "Hernandez",
            phone = "5534567890",
            company = "Globant",
            favorite = true
        ),
        Contacto(
            id = 6,
            first = "Maria",
            last = "Garcia",
            phone = "5545678901",
            email = "maria.garcia@correo.com"
        ),
        Contacto(
            id = 7,
            first = "Carlos",
            last = "Ramirez",
            phone = "5556789012",
            company = "Oracle"
        ),
        Contacto(
            id = 8,
            first = "Sofia",
            last = "Martinez",
            phone = "5567890123",
            email = "sofia@correo.com",
            favorite = true
        ),
        Contacto(id = 9, first = "Diego", last = "Sanchez", phone = "5578901234"),
        Contacto(
            id = 10,
            first = "Valeria",
            last = "Torres",
            phone = "5589012345",
            company = "Mercado Libre"
        ),
        Contacto(
            id = 11,
            first = "Jorge",
            last = "Flores",
            phone = "5590123456",
            email = "jorge.flores@correo.com"
        ),
        Contacto(
            id = 12,
            first = "Camila",
            last = "Reyes",
            phone = "5501234567",
            company = "Spotify",
            favorite = true
        )
    )

    /** Busca por id en los datos de muestra (puente temporal para la navegacion). */
    fun porId(id: Int): Contacto? = lista.find { it.id == id }
}

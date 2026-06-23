package com.yaeldev.cursitodefundamentosandroid.models

object FabricaContactos {

    fun crear(
        tipo: TiposContacto,
        nombre: String,
        telefono: String,
        apodo: String = "",
        empresa: String = "",
        puesto: String = ""
    ): Contacto = when (tipo) {
        TiposContacto.PERSONAL -> ContactoPersonal(nombre, telefono, apodo)
        TiposContacto.TRABAJO -> ContactoTrabajo(nombre, telefono, empresa, puesto)
    }
}

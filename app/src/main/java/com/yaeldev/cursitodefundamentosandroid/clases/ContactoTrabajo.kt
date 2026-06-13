package com.yaeldev.cursitodefundamentosandroid.clases

class ContactoTrabajo(
    nombre:String,
    telefono:String,
    val empresa:String,
    val puesto:String
) : Contacto(nombre,telefono) {

    override fun describir(): String {
        return "Contacto laboral de: $nombre, de la empresa: $empresa, puesto: $puesto"
    }

}
package com.yaeldev.cursitodefundamentosandroid.clases

class ContactoPersonal(
    nombre:String,
    telefono:String,
    val  apodo:String
): Contacto(nombre = nombre, telefono =telefono) {

    override fun describir(): String {
        return "Contacto personal: $nombre alias el $apodo, telefono: $telefono"
    }

}
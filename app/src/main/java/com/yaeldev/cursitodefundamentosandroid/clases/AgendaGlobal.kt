package com.yaeldev.cursitodefundamentosandroid.clases

import android.content.res.Resources

object AgendaGlobal {

    private val contactos = mutableListOf<Contacto>()

    fun agregar(contacto: Contacto){
        contactos.add(contacto)
    }

    fun obtenerContacto(nombre:String): Contacto{
        return contactos.find { nombre == it.nombre } ?: throw Resources.NotFoundException("El contacto con el nombre $nombre, no existe")
    }

    fun eliminar(contacto: Contacto) {
        contactos.remove(contacto)
    }


    fun total():Int = contactos.size

    fun nombres(): List<String> {
        return contactos.map { it.nombre }
    }

}
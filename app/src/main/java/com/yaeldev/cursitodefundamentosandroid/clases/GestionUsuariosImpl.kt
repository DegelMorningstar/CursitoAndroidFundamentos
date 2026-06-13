package com.yaeldev.cursitodefundamentosandroid.clases

class GestionUsuariosImpl(
) : GestionUsuarios {

    fun obtenerTodosLosNombres(): List<String> {
        return AgendaGlobal.nombres()
    }

    override fun guardar(objeto: Contacto) {
        AgendaGlobal.agregar(contacto = objeto)
    }

    override fun obtener(nombre: String): Contacto {
        return AgendaGlobal.obtenerContacto(nombre)
    }

    override fun eliminar(objeto: Contacto) {
        AgendaGlobal.eliminar(objeto)
    }


}
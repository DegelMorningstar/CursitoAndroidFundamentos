package com.yaeldev.cursitodefundamentosandroid.clases

interface GestionUsuarios {
    fun guardar(objeto: Contacto): ResultadoAgenda
    fun obtener(nombre: String): Contacto?
    fun eliminar(objeto: Contacto): ResultadoAgenda
    fun obtenerTodosLosNombres(): List<String>
}

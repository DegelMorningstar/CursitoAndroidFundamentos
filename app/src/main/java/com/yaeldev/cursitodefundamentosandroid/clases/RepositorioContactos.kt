package com.yaeldev.cursitodefundamentosandroid.clases

interface RepositorioContactos {
    fun agregar(contacto: Contacto): ResultadoAgenda
    fun obtener(nombre: String): Contacto?
    fun eliminar(contacto: Contacto): ResultadoAgenda
    fun total(): Int
    fun nombres(): List<String>
    fun describirTodos(): List<String>
}

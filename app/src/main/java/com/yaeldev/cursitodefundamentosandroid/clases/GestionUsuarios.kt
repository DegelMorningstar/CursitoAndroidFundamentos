package com.yaeldev.cursitodefundamentosandroid.clases

interface GestionUsuarios {
    fun guardar(objeto: Contacto)
    fun obtener(nombre:String): Contacto
    fun eliminar(objeto: Contacto)
}
package com.yaeldev.cursitodefundamentosandroid.clases

abstract class Contacto(
    val nombre:String,
    telefono:String
) {
    private var _telefono:String = telefono

    fun actualizarTelefono(nuevo:String){
        _telefono = nuevo
    }

    private fun validarTelefono(valor:String):String {
        val soloDigitos = valor.filter { it.isDigit() }
        return if (soloDigitos.length >= 10) soloDigitos
        else throw IllegalArgumentException("Telefono no valido.")
    }

    abstract fun describir(): String

}
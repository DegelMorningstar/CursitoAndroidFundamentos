package com.yaeldev.cursitodefundamentosandroid.models

abstract class Contacto(
    val nombre: String,
    telefono: String
) {
    // Una sola fuente de verdad para el telefono: encapsulado, validado y
    // solo modificable desde dentro de la jerarquia (private set).
    var telefono: String = validarTelefono(telefono)
        private set

    fun actualizarTelefono(nuevo: String) {
        telefono = validarTelefono(nuevo)
    }

    private fun validarTelefono(valor: String): String {
        val soloDigitos = valor.filter { it.isDigit() }
        return if (soloDigitos.length >= 10) soloDigitos
        else throw IllegalArgumentException("Telefono no valido.")
    }

    abstract fun describir(): String

    // Igualdad por datos de dominio (nombre) para que eliminar/buscar
    // no dependan de la referencia en memoria.
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Contacto) return false
        return nombre == other.nombre
    }

    override fun hashCode(): Int = nombre.hashCode()
}

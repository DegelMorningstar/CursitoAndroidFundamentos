package com.yaeldev.cursitodefundamentosandroid.clases

data class Persona(
    val nombre:String,
    val edad:Int
) {
    fun saludo(){
        println("Hola, mi nombre es ${this.nombre}, mi edad es ${this.edad}")
    }
}
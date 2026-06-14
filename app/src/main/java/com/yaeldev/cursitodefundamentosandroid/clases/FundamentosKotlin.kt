package com.yaeldev.cursitodefundamentosandroid.clases

const val edad:Int = 27

// Antes era fun main(); se renombro para no generar una tarea de ejecucion de
// Gradle (que falla en modulos Android). Llamala desde la app si quieres correrla.
fun demoFundamentosKotlin() {
    val nombre = "Yael"
    val apellidoPaterno = "Montes"
    val apellidoMaterno:String? = null

    val nombreCompleto = "$nombre $apellidoPaterno${ apellidoMaterno ?: ""}"

    val resultado:String = if (edad >= 18) {
        println("es mayor de edad")
        "Ya sufre de alcoholismo"
    } else {
        println("es menor de edad")
        "Aun no sufre de alcoholismo"
    }

    val idioma = "xd"

    val saludo = when(idioma) {
        "Español" -> "Bienvenidos al curso de fundamentos kotlin"
        "Ingles" -> "Welcome to the Kotlin fundamentals course"
        else -> "我不會說尤卡坦語"
    }
    val dia:Int = 2
    val diaSemana = when(dia){
        1 -> "Lunes"
        2 -> "Martes"
        3 -> "Miercoles"
        4 -> "Jueves"
        5 -> "viernes"
        6 -> "sabado"
        7 -> "domingo"
        else -> "Ese dia no existe"
    }

    println("Hola mi nombre es $nombreCompleto y mi edad es $edad, $resultado")

    println(saludo)

    println(diaSemana)

    val edadRango = 25
    when(edadRango) {
        in 1..10 -> println("Estas en tu niñez")
        in 11..17 -> println("Estas en tu adolescencia")
        in 18..50 -> println("Estas en la adultez")
        in 51..70 -> println("Eres adulto mayor")
        else -> println("Se sale de los rangos esperados")
    }

    val tipoX = "Capacitacion"
    when {
        tipoX.isEmpty() -> println("la cadena esta vacia")
        tipoX.length < 5 -> println("es un texto corto")
        else -> println("No hay algo especifico")
    }

    val valorX = "ValorX"
    if(valorX is String) {
        println("El valor x ES UN STRING, y su longitud es: ${valorX.length}")
    }
    val nombres = listOf("Yael","Antonio","Irving","Mariana")
    for(nombre in nombres) {
        println("Los alumnos son: $nombre")
    }


}
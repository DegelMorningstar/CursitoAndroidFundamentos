package com.yaeldev.cursitodefundamentosandroid.clases

sealed class ResultadoAgenda {
    data class Exito(val mesaje:String) : ResultadoAgenda()
    data class Error(val causa: String) : ResultadoAgenda()
    object Cancelado : ResultadoAgenda()
}
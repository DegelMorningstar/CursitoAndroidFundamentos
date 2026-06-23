package com.yaeldev.cursitodefundamentosandroid.models

sealed class ResultadoAgenda {
    data class Exito(val mensaje: String) : ResultadoAgenda()
    data class Error(val causa: String) : ResultadoAgenda()
    object Cancelado : ResultadoAgenda()
}

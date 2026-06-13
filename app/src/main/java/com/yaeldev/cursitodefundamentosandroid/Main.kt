package com.yaeldev.cursitodefundamentosandroid

import com.yaeldev.cursitodefundamentosandroid.clases.ContactoPersonal
import com.yaeldev.cursitodefundamentosandroid.clases.ContactoTrabajo
import com.yaeldev.cursitodefundamentosandroid.clases.GestionUsuariosImpl
import java.lang.Exception

fun main(){

    val yael = ContactoPersonal("Yael","7771234567","Chino")
    val esteban = ContactoTrabajo("Esteban","7771234567","Banco azteca","DBA")

    val gestion = GestionUsuariosImpl()
    gestion.guardar(yael)
    gestion.guardar(esteban)
    
    try {
        println(gestion.obtenerTodosLosNombres())
    }catch (e: Exception){
        println(e.toString())
    }


}
package com.yaeldev.cursitodefundamentosandroid.models

class GestionUsuariosImpl(
    // Se inyecta el repositorio: ya no depende directamente del singleton global,
    // por lo que es sustituible y testeable.
    private val repositorio: RepositorioContactos
) : GestionUsuarios {

    override fun guardar(objeto: Contacto): ResultadoAgenda =
        repositorio.agregar(objeto)

    override fun obtener(nombre: String): Contacto? =
        repositorio.obtener(nombre)

    override fun eliminar(objeto: Contacto): ResultadoAgenda =
        repositorio.eliminar(objeto)

    override fun obtenerTodosLosNombres(): List<String> =
        repositorio.nombres()
}

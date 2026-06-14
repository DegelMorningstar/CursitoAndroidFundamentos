package com.yaeldev.cursitodefundamentosandroid.clases

// Traduce un ResultadoAgenda a texto. El 'when' sobre la sealed class es
// exhaustivo: el compilador obliga a cubrir todos los casos.
fun describirResultado(resultado: ResultadoAgenda): String = when (resultado) {
    is ResultadoAgenda.Exito -> "OK -> ${resultado.mensaje}"
    is ResultadoAgenda.Error -> "ERROR -> ${resultado.causa}"
    ResultadoAgenda.Cancelado -> "Operacion cancelada"
}

// Ejecuta la misma demo que antes vivia en main(), pero acumula la salida en un
// String para poder mostrarla en la pantalla de la app (en vez de println).
// Asi evitamos tener una fun main() dentro de un modulo Android.
fun ejecutarDemoAgenda(): String {
    val sb = StringBuilder()
    fun log(linea: String = "") = sb.appendLine(linea)

    // Se programa contra la interfaz GestionUsuarios, inyectando el repositorio.
    val gestion: GestionUsuarios = GestionUsuariosImpl(AgendaGlobal)

    log("===== ALTA DE CONTACTOS (fabrica + enum) =====")
    // Creacion polimorfica via fabrica usando TiposContacto.
    val ana = FabricaContactos.crear(
        tipo = TiposContacto.PERSONAL,
        nombre = "Ana",
        telefono = "5512345678",
        apodo = "Anita"
    )
    val luis = FabricaContactos.crear(
        tipo = TiposContacto.TRABAJO,
        nombre = "Luis",
        telefono = "5598765432",
        empresa = "YaelDev",
        puesto = "Backend"
    )

    log(describirResultado(gestion.guardar(ana)))
    log(describirResultado(gestion.guardar(luis)))

    // Intento de duplicado: equals/hashCode por nombre detecta la colision.
    val anaDuplicada = ContactoPersonal("Ana", "5500000000", "Otra Ana")
    log(describirResultado(gestion.guardar(anaDuplicada)))

    log()
    log("===== POLIMORFISMO (describir) =====")
    AgendaGlobal.describirTodos().forEach { log(it) }

    log()
    log("===== ENCAPSULAMIENTO + VALIDACION =====")
    ana.actualizarTelefono("55-1111-2222")          // se sanea a digitos
    log("Telefono de Ana actualizado: ${ana.telefono}")
    try {
        ana.actualizarTelefono("123")               // invalido: lanza excepcion
    } catch (e: IllegalArgumentException) {
        log("Validacion funcionando: ${e.message}")
    }

    log()
    log("===== CONSULTA (nullable) =====")
    val encontrado = gestion.obtener("Luis")
    log(encontrado?.describir() ?: "No encontrado")
    val inexistente = gestion.obtener("Pedro")
    log(inexistente?.describir() ?: "Contacto 'Pedro' no existe (null seguro)")

    log()
    log("===== BAJA =====")
    log(describirResultado(gestion.eliminar(ana)))
    log(describirResultado(gestion.eliminar(ana)))   // ya no esta -> Error

    log()
    log("===== ESTADO FINAL =====")
    log("Nombres en agenda: ${gestion.obtenerTodosLosNombres()}")
    log("Total de contactos: ${AgendaGlobal.total()}")

    return sb.toString()
}

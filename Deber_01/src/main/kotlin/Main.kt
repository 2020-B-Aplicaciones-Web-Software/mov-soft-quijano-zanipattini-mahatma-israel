import DAO.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun main(args: Array<String>) {

    // Objetos DAO para cada tipo de Entidad
    val edificioDAO = EdificioDAO()
    val departamentoDAO = DepartamentoDAO()

    // Bucle de ejecucion
    while (true) {
        imprimirMenu()
        val entrada = readLine()
        when (entrada) {
            // Visualizar Edificios
            ("1") -> {
                println("----- Edificios disponibles -----")
                var edificios = edificioDAO.leerEdificios()
                edificios.forEachIndexed { index, edificio ->
                    println("${index + 1}.-\t${edificio.nombre}")
                }
                imprimirMenuCRUD()
                val accion = readLine()
                when (accion) {
                    // Create
                    ("1") -> {
                        println("Ingrese el nombre del nuevo Edificio")
                        val nombre = readLine()!!
                        println("Ingrese el numero de pisos del nuevo Edificio")
                        val numPisos = readLine()!!
                        println("Ingrese el area (m2) del nuevo Edificio")
                        val area = readLine()!!
                        println("Ingrese la fecha de apertura (YYYY-MM-DD) del nuevo Edificio")
                        val fecha = readLine()!!
                        val localDate = LocalDate.parse(fecha, DateTimeFormatter.ISO_DATE)
                        println("Ingrese la direccion del nuevo Edificio")
                        val dir = readLine()!!
                        val nuevoEdificio = Edificio(nombre, numPisos.toInt(), area.toFloat(), localDate, dir)
                        edificioDAO.crearEdificio(nuevoEdificio)
                    }
                    // Read
                    ("2") -> {
                        println("Ingrese el nombre del Edificio cuya informacion desea visualizar")
                        val nombre = readLine()!!
                        val edificio = edificioDAO.leerEdificio(nombre)
                        if (edificio != null) {
                            println(edificio)
                        } else {
                            println("El nombre ${nombre} no se pudo encontrar")
                        }
                    }
                    // Update
                    ("3") -> {
                        println("Ingrese el nombre del Edificio cuya informacion desea actualizar")
                        val nombreAnterior = readLine()!!
                        if (edificioDAO.leerEdificio(nombreAnterior) != null) {
                            println("Ingrese el nuevo nombre del Edificio")
                            val nombre = readLine()!!
                            println("Ingrese el nuevo numero de pisos del Edificio")
                            val numPisos = readLine()!!
                            println("Ingrese la nueva area (m2) del Edificio")
                            val area = readLine()!!
                            println("Ingrese la nueva fecha de apertura (YYYY-MM-DD) del Edificio")
                            val fecha = readLine()!!
                            val localDate = LocalDate.parse(fecha, DateTimeFormatter.ISO_DATE)
                            println("Ingrese la nueva direccion del Edificio")
                            val dir = readLine()!!
                            val actualizado = Edificio(nombre, numPisos.toInt(), area.toFloat(), localDate, dir)
                            val anterior = edificioDAO.leerEdificio(nombreAnterior)
                            edificioDAO.actualizarEdificio(anterior!!, actualizado)
                        } else {
                            println("El nombre ${nombreAnterior} no se pudo encontrar")
                        }
                    }
                    // Delete
                    ("4") -> {
                        println("Ingrese el nombre del Edificio que desea eliminar")
                        val nombre = readLine()!!
                        val edificio = edificioDAO.leerEdificio(nombre)
                        if (edificio != null) {
                            edificioDAO.eliminarEdificio(edificio)
                        } else {
                            println("El nombre ${nombre} no se pudo encontrar")
                        }
                    }
                }
            }
            // Visualizar Departamentos
            ("2") -> {
                println("----- Departamentos disponibles -----")
                var departamentos = departamentoDAO.leerDepartamentos()
                departamentos.forEachIndexed { index, departamento ->
                    println("${index + 1}.-\t${departamento.nombre}")
                }
                imprimirMenuCRUD()
                val accion = readLine()
                when (accion) {
                    // Create
                    ("1") -> {
                        println("Ingrese el nombre del nuevo Departamento")
                        val nombre = readLine()!!
                        println("Ingrese el numero de habitaciones del nuevo Departamento")
                        val numHab = readLine()!!
                        println("Ingrese el numero de banos del nuevo Departamento")
                        val numBanos = readLine()!!
                        println("Ingrese el area (m2) del nuevo Departamento")
                        val area = readLine()!!
                        println("Ingrese el valor ($) del nuevo Departamento")
                        val valor = readLine()!!
                        val nuevoDepartamento = Departamento(nombre, numHab.toInt(), numBanos.toInt(), area.toFloat(), valor.toFloat())
                        departamentoDAO.crearDepartamento(nuevoDepartamento)
                    }
                    // Read
                    ("2") -> {
                        println("Ingrese el nombre del Departamento cuya informacion desea visualizar")
                        val nombre = readLine()!!
                        val departamento = departamentoDAO.leerDepartamento(nombre)
                        if (departamento != null) {
                            println(departamento)
                        } else {
                            println("El nombre ${nombre} no se pudo encontrar")
                        }
                    }
                    // Update
                    ("3") -> {
                        imprimirMenuActualizar()
                        val opcion = readLine()
                        when (opcion) {
                            ("1") -> {
                                println("Ingrese el nombre del Departamento cuya informacion desea actualizar")
                                    val nombreAnterior = readLine()!!
                                if (departamentoDAO.leerDepartamento(nombreAnterior) != null) {
                                    println("Ingrese el nuevo nombre del Departamento")
                                    val nombre = readLine()!!
                                    println("Ingrese el nuevo numero de habitaciones del Departamento")
                                    val numHab = readLine()!!
                                    println("Ingrese el nuevo numero de banos del Departamento")
                                    val numBanos = readLine()!!
                                    println("Ingrese la nueva area (m2) del Departamento")
                                    val area = readLine()!!
                                    println("Ingrese el nuevo valor ($) del Departamento")
                                    val valor = readLine()!!
                                    val actualizado = Departamento(
                                        nombre,
                                        numHab.toInt(),
                                        numBanos.toInt(),
                                        area.toFloat(),
                                        valor.toFloat()
                                    )
                                    val anterior = departamentoDAO.leerDepartamento(nombreAnterior)
                                    departamentoDAO.actualizarDepartamento(anterior!!, actualizado)
                                } else {
                                    println("El nombre ${nombreAnterior} no se pudo encontrar")
                                }
                            }
                            ("2") -> {
                                println("Ingrese el nombre del Departamento que quiere asignar:")
                                val nombreDpto = readLine()
                                println("Ingrese el nombre del Edificio al que lo quiere asignar:")
                                val nombreEdif = readLine()
                                val ed = edificioDAO.leerEdificio(nombreEdif!!)
                                val dpto = departamentoDAO.leerDepartamento(nombreDpto!!)
                                if ((ed != null) && (dpto != null)) {
                                    departamentoDAO.asignarEdificio(ed, dpto)
                                } else {
                                    println("Alguno de los datos es incorrecto")
                                }
                            }
                        }
                    }
                    // Delete
                    ("4") -> {
                        println("Ingrese el nombre del Departamento que desea eliminar")
                        val nombre = readLine()!!
                        val departamento = departamentoDAO.leerDepartamento(nombre)
                        if (departamento != null) {
                            departamentoDAO.eliminarDepartamento(departamento)
                        } else {
                            println("El nombre ${nombre} no se pudo encontrar")
                        }
                    }
                }
            }
            // Salir
            ("0") -> {
                break
            }
        }
    }
    println("Gracias por usar el sistema!")


}

fun imprimirMenu() {
    println("|-------------------------------------|\n" +
            "| Sistema de Gestion de Departamentos |\n" +
            "|-------------------------------------|\n" +
            "| Seleccione con que datos trabajar   |\n" +
            "| 1.- Edificios                       |\n" +
            "| 2.- Departamentos                   |\n" +
            "| 0.- Salir                           |\n" +
            "|-------------------------------------|\n" +
            "Ingrese su opcion:")
}

fun imprimirMenuCRUD() {
    println("Que accion desea realizar?\n" +
            "1.- Crear\n" +
            "2.- Leer informacion\n" +
            "3.- Actualizar\n" +
            "4.- Eliminar")
}

fun imprimirMenuActualizar() {
    println("Que desea realizar?\n" +
            "1.- Actualizar los datos de un Departamento\n" +
            "2.- Asignar un Departamento a un Edificio")
}
package DAO

import Edificio
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EdificioDAO {

    companion object {
        // Path del archivo de persistencia de los Edificios
        val edificiosPATH = "C:\\Users\\mahis\\Documents\\Github\\mov-soft-quijano-zanipattini-mahatma-israel\\Deber_01\\src\\main\\resources\\edificios.txt"

        // Path del archivo de persistencia de los Departamentos de cada Edificio
        val oneToManyPATH = "C:\\Users\\mahis\\Documents\\Github\\mov-soft-quijano-zanipattini-mahatma-israel\\Deber_01\\src\\main\\resources\\edificioDepartamentos.txt"
    }

    // Create
    fun crearEdificio(nuevo: Edificio) {
        // Si el edificio no esta ya registrado
        if (leerEdificio(nuevo.nombre) == null) {
            val input = File(edificiosPATH).inputStream()
            val filas = mutableListOf<String>()
            input.bufferedReader().useLines { linea -> linea.forEach { filas.add(it) } }
            // Se obtiene el mayor ID ya registrado
            var maxID: Int = 0
            filas.forEach {
                val attr = it.trim().split(",")
                if (attr[0].toInt() > maxID) {
                    maxID = attr[0].toInt()
                }
            }
            nuevo.id = maxID + 1    // El nuevo edificio tendra el siguiente ID
            File(edificiosPATH).appendText("\n"+ nuevo.getRegisterString())  // Se registra el nuevo edificio
        } else {
            println("El edificio ${nuevo.nombre} ya esta registrado!")
        }
    }

    // Read
    fun leerEdificio(nombre: String): Edificio? {
        val input = File(edificiosPATH).inputStream()
        val filas = mutableListOf<String>()
        input.bufferedReader().useLines { linea -> linea.forEach { filas.add(it) } }
        // Itera por cada fila
        filas.forEach { fila ->
            val attr = fila.trim().split(",")
            if (attr[1] == nombre) {    // Si encuentra un edificio con el nombre que se busca
                val numPisos = attr[2].toInt()
                val area = attr[3].toFloat()
                val fecha = LocalDate.parse(attr[4], DateTimeFormatter.ISO_DATE)
                val direccion = attr[5]
                val edificio = Edificio(nombre, numPisos, area, fecha, direccion)
                edificio.id = attr[0].toInt()   // Se agrega al objeto el ID registrado en el archivo

                // Se agregan los Departamentos de un Edificio
                val input_2 = File(oneToManyPATH).inputStream()
                val relaciones = mutableListOf<String>()
                input_2.bufferedReader().useLines { linea -> linea.forEach { relaciones.add(it) } }
                // Iterar por cada relacion
                relaciones.forEach{ relacion ->
                    // Al encontrar el ID del Edificio
                    if (edificio.id == relacion.split(":")[0].toInt()) {
                        val departamentos = relacion.trim().split(":")[1].split(",")
                        // Si existen Departamentos asignados
                        if (departamentos.isNotEmpty()) {
                            departamentos.forEach {
                                if (it.length > 0) {
                                    val departamentoDAO = DepartamentoDAO()
                                    val departamento = departamentoDAO.leerDepartamentoPorID(it.toInt())
                                    if (departamento != null)
                                        edificio.agregarDepartamento(departamento)
                                }
                            }
                        }
                    }
                }

                return edificio
            }
        }
        return null     // El edificio no esta registrado
    }

    fun leerEdificioPorID(id: Int): Edificio? {
        val input = File(edificiosPATH).inputStream()
        val filas = mutableListOf<String>()
        input.bufferedReader().useLines { linea -> linea.forEach { filas.add(it) } }
        // Itera por cada fila
        filas.forEach {
            val attr = it.trim().split(",")
            if (attr[0].toInt() == id) {    // Si encuentra un edificio con el ID que se busca
                val nombre = attr[1]
                val numPisos = attr[2].toInt()
                val area = attr[3].toFloat()
                val fecha = LocalDate.parse(attr[4], DateTimeFormatter.ISO_DATE)
                val direccion = attr[5]
                val edificio = Edificio(nombre, numPisos, area, fecha, direccion)
                edificio.id = id   // Se agrega al objeto el ID indicado
                return edificio
            }
        }
        return null     // El edificio no esta registrado
    }

    fun leerEdificios(): MutableList<Edificio> {
        val edificios = mutableListOf<Edificio>()
        val input = File(edificiosPATH).inputStream()
        val filas = mutableListOf<String>()
        input.bufferedReader().useLines { linea -> linea.forEach { filas.add(it) } }
        // Itera por cada fila
        filas.forEach {
            val attr = it.trim().split(",")
            val id = attr[0].toInt()
            val nombre = attr[1]
            val numPisos = attr[2].toInt()
            val area = attr[3].toFloat()
            val fecha = LocalDate.parse(attr[4], DateTimeFormatter.ISO_DATE)
            val direccion = attr[5]
            val edificio = Edificio(nombre, numPisos, area, fecha, direccion)
            edificio.id = id   // Se agrega al objeto el ID indicado
            edificios.add(edificio)
        }
        return edificios
    }


    // Update
    fun actualizarEdificio(anterior: Edificio, actualizado: Edificio) {
        val filas = mutableListOf<String>()
        val input = File(edificiosPATH).inputStream()
        input.bufferedReader().useLines { linea -> linea.forEach { filas.add(it) } }
        var newContent = "" // Almacena el nuevo contenido que sobreescribe en el archivo
        filas.forEachIndexed { index, it ->
            val attr = it.trim().split(",")
            // Si encuentra el edificio indicado, actualiza su informacion
            if (attr[1] == anterior.nombre) {
                actualizado.id = anterior.id
                filas[index] = actualizado.getRegisterString()
            }
            newContent += filas[index]  // El resto de informacion no cambia
            if (index < filas.size - 1) {
                newContent += "\n"
            }
        }
        File(edificiosPATH).printWriter().use { param -> param.println(newContent) }
    }

    // Delete
    fun eliminarEdificio(eliminado: Edificio) {
        val filas = mutableListOf<String>()
        val input = File(edificiosPATH).inputStream()
        input.bufferedReader().useLines { linea -> linea.forEach { filas.add(it) } }
        var newContent = "" // Almacena el nuevo contenido que sobreescribe en el archivo
        filas.forEachIndexed { index, it ->
            val attr = it.trim().split(",")
            // Evita registrar la informacion del edificio indicado
            if (attr[1] != eliminado.nombre) {
                newContent += filas[index].trim()
                if (index < filas.size - 1) {
                    newContent += "\n"
                }
            }
        }
        File(edificiosPATH).printWriter().use { param -> param.println(newContent.trim()) }
    }
}
package DAO

import Departamento
import Edificio
import java.io.File

class DepartamentoDAO {

    companion object {
        // Path del archivo de persistencia de los Departamentos
        val departamentosPATH = "C:\\Users\\mahis\\Documents\\Github\\mov-soft-quijano-zanipattini-mahatma-israel\\Deber_01\\src\\main\\resources\\departamentos.txt"

        // Path del archivo de persistencia de los Departamentos de cada Edificio
        val oneToManyPATH = "C:\\Users\\mahis\\Documents\\Github\\mov-soft-quijano-zanipattini-mahatma-israel\\Deber_01\\src\\main\\resources\\edificioDepartamentos.txt"
    }

    // Create
    fun crearDepartamento(nuevo: Departamento) {
        // Si el departamento no esta ya registrado
        if (leerDepartamento(nuevo.nombre) == null) {
            val input = File(departamentosPATH).inputStream()
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
            nuevo.id = maxID + 1    // El nuevo departamento tendra el siguiente ID
            File(departamentosPATH).appendText("\n"+ nuevo.getRegisterString())  // Se registra el nuevo departamento
        } else {
            println("El departamento ${nuevo.nombre} ya esta registrado!")
        }
    }

    // Read
    fun leerDepartamento(nombre: String): Departamento? {
        val input = File(departamentosPATH).inputStream()
        val filas = mutableListOf<String>()
        input.bufferedReader().useLines { linea -> linea.forEach { filas.add(it) } }
        // Itera por cada fila
        filas.forEach { fila ->
            val attr = fila.trim().split(",")
            if (attr[1] == nombre) {    // Si encuentra un departamento con el nombre que se busca
                var numeroHabitaciones = attr[2].toInt()
                var numeroBanos = attr[3].toInt()
                var areaM2 = attr[4].toFloat()
                var valor = attr[5].toFloat()
                val departamento = Departamento(nombre, numeroHabitaciones, numeroBanos, areaM2, valor)
                departamento.id = attr[0].toInt()   // Se agrega al objeto el ID registrado en el archivo

                // Se agrega el Edificio al que pertenece el Departamento
                val input_2 = File(oneToManyPATH).inputStream()
                val relaciones = mutableListOf<String>()
                input_2.bufferedReader().useLines { linea -> linea.forEach { relaciones.add(it) } }
                // Iterar por cada relacion
                relaciones.forEach{ relacion ->
                    val departamentos = relacion.trim().split(":")[1].split(",")
                    // Si hay Departamentos registrados en un Edificio
                    if (departamentos.isNotEmpty()) {
                        departamentos.forEach { dpto ->
                            if (dpto.toInt() == departamento.id) {  // Si se encuentra el ID indicado
                                val edificioDAO = EdificioDAO()
                                val edificio = edificioDAO.leerEdificioPorID(relacion.split(":")[0].toInt())
                                if (edificio != null)
                                    departamento.edificio = edificio
                            }
                        }
                    }
                }

                return departamento
            }
        }
        return null     // El departamento no esta registrado
    }

    fun leerDepartamentoPorID(id: Int): Departamento? {
        val input = File(departamentosPATH).inputStream()
        val filas = mutableListOf<String>()
        input.bufferedReader().useLines { linea -> linea.forEach { filas.add(it) } }
        // Itera por cada fila
        filas.forEach {
            val attr = it.trim().split(",")
            if (attr[0].toInt() == id) {    // Si encuentra un departamento con el ID que se busca
                var nombre = attr[1]
                var numeroHabitaciones = attr[2].toInt()
                var numeroBanos = attr[3].toInt()
                var areaM2 = attr[4].toFloat()
                var valor = attr[5].toFloat()
                val departamento = Departamento(nombre, numeroHabitaciones, numeroBanos, areaM2, valor)
                departamento.id = id   // Se agrega al objeto el ID indicado
                return departamento
            }
        }
        return null     // El departamento no esta registrado
    }

    fun leerDepartamentos(): MutableList<Departamento> {
        val departamentos = mutableListOf<Departamento>()
        val input = File(departamentosPATH).inputStream()
        val filas = mutableListOf<String>()
        input.bufferedReader().useLines { linea -> linea.forEach { filas.add(it) } }
        // Itera por cada fila
        filas.forEach {
            val attr = it.trim().split(",")
            var id = attr[0].toInt()
            var nombre = attr[1]
            var numeroHabitaciones = attr[2].toInt()
            var numeroBanos = attr[3].toInt()
            var areaM2 = attr[4].toFloat()
            var valor = attr[5].toFloat()
            val departamento = Departamento(nombre, numeroHabitaciones, numeroBanos, areaM2, valor)
            departamento.id = id   // Se agrega al objeto el ID indicado
            departamentos.add(departamento)
        }
        return departamentos
    }


    // Update
    fun actualizarDepartamento(anterior: Departamento, actualizado: Departamento) {
        val filas = mutableListOf<String>()
        val input = File(departamentosPATH).inputStream()
        input.bufferedReader().useLines { linea -> linea.forEach { filas.add(it) } }
        var newContent = "" // Almacena el nuevo contenido que sobreescribe en el archivo
        filas.forEachIndexed { index, it ->
            val attr = it.trim().split(",")
            // Si encuentra el departamento indicado, actualiza su informacion
            if (attr[1] == anterior.nombre) {
                actualizado.id = anterior.id
                filas[index] = actualizado.getRegisterString()
            }
            newContent += filas[index]  // El resto de informacion no cambia
            if (index < filas.size - 1) {
                newContent += "\n"
            }
        }
        File(departamentosPATH).printWriter().use { param -> param.println(newContent) }
    }

    fun asignarEdificio(edificio: Edificio, departamento: Departamento) {
        val filas = mutableListOf<String>()
        val input = File(oneToManyPATH).inputStream()
        input.bufferedReader().useLines { linea -> linea.forEach { filas.add(it) } }
        var flag = false
        var newContent = "" // Almacena el nuevo contenido que sobreescribe en el archivo
        filas.forEachIndexed { index, it ->
            val dptos = it.trim().split(":")[1].split(",")
            // Si encuentra el departamento indicado, actualiza su informacion
            dptos.forEach {
                if (it.toInt() == departamento.id) {
                    flag = true
                    println("El departamento ${departamento.nombre} ya tiene un edificio asignado!")
                }
            }
        }
        if (!flag) {
            var found = false
            filas.forEachIndexed { index, it ->
                val edificioID = it.trim().split(":")[0]
                // Si encuentra el edificio indicado
                if (edificioID.toInt() == edificio.id) {
                    found = true
                    filas[index] += ",${departamento.id}" // Se agrega el departamento
                }
                newContent += filas[index]  // El resto de informacion no cambia
                if (index < filas.size - 1) {
                    newContent += "\n"
                }
            }
            if (!found) {
                newContent += "\n${edificio.id}:${departamento.id}"
            }
            File(oneToManyPATH).printWriter().use { param -> param.println(newContent) }
        }
    }

    // Delete
    fun eliminarDepartamento(eliminado: Departamento) {
        val filas = mutableListOf<String>()
        val input = File(departamentosPATH).inputStream()
        input.bufferedReader().useLines { linea -> linea.forEach { filas.add(it) } }
        var newContent = "" // Almacena el nuevo contenido que sobreescribe en el archivo
        filas.forEachIndexed { index, it ->
            val attr = it.trim().split(",")
            // Evita registrar la informacion del departamento indicado
            if (attr[1] != eliminado.nombre) {
                newContent += filas[index].trim()
                if (index < filas.size - 1) {
                    newContent += "\n"
                }
            }
        }
        File(departamentosPATH).printWriter().use { param -> param.println(newContent.trim()) }
    }
}
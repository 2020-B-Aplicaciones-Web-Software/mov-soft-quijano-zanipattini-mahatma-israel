package com.example.examen01.sqlitehelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.examen01.entities.Departamento
import com.example.examen01.entities.Edificio
import java.text.SimpleDateFormat

class SQLiteHelper(
    context: Context?
): SQLiteOpenHelper(context, "Examen01", null, 1) {

    // Se crea la tabla Edificio al crear la actividad
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptCrearTablaEdificio =
            """
                CREATE TABLE Edificio (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre VARCHAR(20),
                    numeroPisos INTEGER,
                    area DECIMAL(4,2),
                    fechaApertura DATETIME,
                    direccion VARCHAR(20)
                )
            """.trimIndent()
        val scriptCrearTablaDepartamento =
            """
                CREATE TABLE Departamento (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre VARCHAR(20),
                    numeroHabitaciones INTEGER,
                    numeroBanos INTEGER,
                    area DECIMAL(4,2),
                    valor DECIMAL(7,2),
                    edificioID INTEGER,
                    FOREIGN KEY (edificioID) REFERENCES Edificio(id) ON DELETE CASCADE
                )
            """.trimIndent()
        Log.i("BaseDatos", "Creando base de datos")
        db?.execSQL(scriptCrearTablaEdificio)   // Crea tabla Edificio
        db?.execSQL(scriptCrearTablaDepartamento)   // Crea tabla Departamento
    }

    companion object {
        val sdf = SimpleDateFormat("dd-MM-yyyy")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //TODO("Not yet implemented")
    }

    /* ------------ Edificio ------------ */
    //Create
    fun crearEdificio(
        nombre: String, numeroPisos: Int,
        area: Float, fecha: String,
        direccion: String
    ): Boolean {
        val conexionEscritura = writableDatabase

        val valores = ContentValues()
        valores.put("nombre", nombre)
        valores.put("numeroPisos", numeroPisos)
        valores.put("area", area)
        valores.put("fechaApertura", fecha)
        valores.put("direccion", direccion)

        val resultadoEscritura: Long = conexionEscritura.insert(
            "Edificio",
            null,
            valores
        )
        conexionEscritura.close()
        return resultadoEscritura.toInt() != -1
    }

    // Read
    fun leerEdificios(): MutableList<Edificio> {
        val scriptLeerEdificios = "SELECT * FROM Edificio"

        val conexionLectura = readableDatabase

        val resultadoLectura = conexionLectura.rawQuery(
            scriptLeerEdificios,
            null
        )

        val existeEdificio = resultadoLectura.moveToFirst()

        val listaEdificios = arrayListOf<Edificio>()

        if (existeEdificio) {
            do {
                val id = resultadoLectura.getInt(0) // ID
                val nombre = resultadoLectura.getString(1) // Nombre
                val numPisos = resultadoLectura.getInt(2) // Numero de Pisos
                val area = resultadoLectura.getFloat(3) // Area (m2)
                val fechaString = resultadoLectura.getString(4)   // Fecha de apertura
                val fecha = sdf.parse(fechaString)
                val direccion = resultadoLectura.getString(5)   // Direccion
                if (id != null) {
                    listaEdificios.add(
                        Edificio(id, nombre, numPisos, area, fecha, direccion)
                    )
                }
            } while (resultadoLectura.moveToNext()) // Mientras haya mas Edificios
        }

        resultadoLectura.close()
        conexionLectura.close()

        return listaEdificios
    }

    // Update
    fun actualizarEdificio(
        id: Int, nombre: String,
        numeroPisos: Int, area: Float,
        fecha: String, direccion: String
    ): Boolean {
        val conexionEscritura = writableDatabase

        val valores = ContentValues()
        valores.put("nombre", nombre)
        valores.put("numeroPisos", numeroPisos)
        valores.put("area", area)
        valores.put("fechaApertura", fecha)
        valores.put("direccion", direccion)

        val resultadoActualizar = conexionEscritura.update(
            "Edificio",  // Nombre de la tabla
            valores, // Valores a actualizar
            "id=?", // Clausula WHERE
            arrayOf( // Parametros clausula WHERE
                id.toString()
            )
        )

        conexionEscritura.close()
        return resultadoActualizar != -1
    }

    // Delete
    fun eliminarEdificioPorID(id: Int): Boolean {
        val conexionEscritura = writableDatabase
        val resultadoEliminar = conexionEscritura.delete(
            "Edificio",  // Nombre de la tabla
            "id=?", // Clausula WHERE
            arrayOf( // Parametros clausula WHERE
                id.toString()
            )
        )
        conexionEscritura.close()
        return resultadoEliminar != -1
    }


    /* ------------ Departamento ------------ */
    // Create
    fun crearDepartamento(
        nombre: String, numeroHabitaciones: Int,
        numeroBanos: Int, area: Float,
        valor: Float, idEdificio: Int
    ): Boolean {
        val conexionEscritura = writableDatabase

        val valores = ContentValues()
        valores.put("nombre", nombre)
        valores.put("numeroHabitaciones", numeroHabitaciones)
        valores.put("numeroBanos", numeroBanos)
        valores.put("area", area)
        valores.put("valor", valor)
        valores.put("edificioID", idEdificio)

        val resultadoEscritura: Long = conexionEscritura.insert(
            "Departamento",
            null,
            valores
        )
        conexionEscritura.close()
        return resultadoEscritura.toInt() != -1
    }

    // Read
    fun leerDepartamentos(edificio: Edificio): MutableList<Departamento> {
        val edificioID = edificio.id
        val scriptLeerDepartamentos = "SELECT * FROM Departamento WHERE edificioID=$edificioID"

        val conexionLectura = readableDatabase

        val resultadoLectura = conexionLectura.rawQuery(
            scriptLeerDepartamentos,
            null
        )

        val existeDepartamento = resultadoLectura.moveToFirst()

        val listaDepartamentos = arrayListOf<Departamento>()

        if (existeDepartamento) {
            do {
                val id = resultadoLectura.getInt(0) // ID
                val nombre = resultadoLectura.getString(1)  // Nombre
                val numHabitaciones = resultadoLectura.getInt(2)    // Numero de habitaciones
                val numBanos = resultadoLectura.getInt(3)   // Numero de Banos
                val area = resultadoLectura.getFloat(4) // Area (m2)
                val valor = resultadoLectura.getFloat(5)    // Valor ($)

                if (id != null) {
                    val dpto = Departamento(id, nombre, numHabitaciones, numBanos, area, valor)
                    dpto.edificio = edificio    // Se asigna el Edificio
                    listaDepartamentos.add(dpto)
                }
            } while (resultadoLectura.moveToNext()) // Mientras haya mas Departamentos
        }

        resultadoLectura.close()
        conexionLectura.close()

        return listaDepartamentos
    }

    // Update
    fun actualizarDepartamento(
        id: Int, nombre: String,
        numHabitaciones: Int, numBanos: Int,
        area: Float, valor: Float
    ): Boolean {
        val conexionEscritura = writableDatabase

        val valores = ContentValues()
        valores.put("nombre", nombre)
        valores.put("numeroHabitaciones", numHabitaciones)
        valores.put("numeroBanos", numBanos)
        valores.put("area", area)
        valores.put("valor", valor)

        val resultadoActualizar = conexionEscritura.update(
            "Departamento",  // Nombre de la tabla
            valores, // Valores a actualizar
            "id=?", // Clausula WHERE
            arrayOf( // Parametros clausula WHERE
                id.toString()
            )
        )

        conexionEscritura.close()
        return resultadoActualizar != -1
    }

    // Delete
    fun eliminarDepartamentoPorID(id: Int): Boolean {
        val conexionEscritura = writableDatabase
        val resultadoEliminar = conexionEscritura.delete(
            "Departamento",  // Nombre de la tabla
            "id=?", // Clausula WHERE
            arrayOf( // Parametros clausula WHERE
                id.toString()
            )
        )
        conexionEscritura.close()
        return resultadoEliminar != -1
    }
}
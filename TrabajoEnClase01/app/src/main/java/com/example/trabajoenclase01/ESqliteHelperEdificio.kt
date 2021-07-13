package com.example.trabajoenclase01

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class ESqliteHelperEdificio(
    context: Context?
): SQLiteOpenHelper(context, "trabajoEnClase01", null, 1) {

    // Se crea la tabla Edificio al crear la actividad
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptCrearTablaEdificio =
            """
                CREATE TABLE Edificio (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre VARCHAR(20),
                    numeroPisos INTEGER
                )
            """.trimIndent()
        Log.i("BDD", "Creando la tabla Edificio")
        db?.execSQL(scriptCrearTablaEdificio)   // Ejecuta la sentencia SQL
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //TODO("Not yet implemented")
    }

    //Create
    fun crearEdificio(nombre: String, numeroPisos: Int): Boolean {
        val conexionEscritura = writableDatabase

        val valores = ContentValues()
        valores.put("nombre", nombre)
        valores.put("numeroPisos", numeroPisos)

        val resultadoEscritura: Long = conexionEscritura.insert(
            "Edificio",
            null,
            valores
        )
        conexionEscritura.close()
        return resultadoEscritura.toInt() != -1
    }

    //Read
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
                if (id != null) {
                    listaEdificios.add(
                        Edificio(id, nombre, numPisos)
                    )
                }
            } while (resultadoLectura.moveToNext()) // para varios Usuarios
        }

        resultadoLectura.close()
        conexionLectura.close()

        return listaEdificios
    }

    //Update
    fun actualizarEdificio(id: Int, nombre: String, numeroPisos: Int): Boolean {
        val conexionEscritura = writableDatabase

        val valores = ContentValues()
        valores.put("nombre", nombre)
        valores.put("numeroPisos", numeroPisos)

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

    //Delete
    fun eliminarEdificioPorID(id: Int): Boolean {
        val conexionEscritura = writableDatabase
        val resultadoEliminar = conexionEscritura.delete(
            "Edificio",
            "id=?",
            arrayOf(
                id.toString()
            )
        )
        conexionEscritura.close()
        return resultadoEliminar != -1
    }

}
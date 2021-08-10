package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class ESqliteHelperUsuario (
    contexto: Context?
): SQLiteOpenHelper (
    contexto,
    "moviles",
    null,
    1
){
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptCrearTablaUsuario =
            """
                CREATE TABLE USUARIO (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre VARCHAR(50),
                    descripcion varchar(50)
                )
            """.trimIndent()
        Log.i("bdd", "Creando la tabla de usuario")
        db?.execSQL(scriptCrearTablaUsuario)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    fun crearUsuarioFormulario (
        nombre: String,
        descripcion: String
    ): Boolean {
        val conexionEscritura = writableDatabase

        val valoresAGuardar = ContentValues()
        valoresAGuardar.put("nombre", nombre)
        valoresAGuardar.put("descripcion", descripcion)

        val resultadoEscritura: Long = conexionEscritura.insert(
            "USUARIO",
            null,
            valoresAGuardar
        )
        conexionEscritura.close()
        return resultadoEscritura.toInt() != -1
    }

    fun consultarUsuarioPorId(id: Int): EUsuarioBDD {
        val scriptConsultarUsuario = "SELECT * FROM USUARIO WHERE ID = ${id}"

        val baseDatosLectura = readableDatabase

        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            scriptConsultarUsuario,
            null
        )

        val existeUsuario = resultadoConsultaLectura.moveToFirst()
        val usuarioEncontrado = EUsuarioBDD(0, "", "")
        // val arregloUsuarios = arrayListOf<EUsuarioBDD>()     // Otra forma (Si se quiere varios)

        if (existeUsuario) {
            do {
                // Al usar el * en la consulta, se coloca en el orden de las columnas
                val id = resultadoConsultaLectura.getInt(0) // ID
                val nombre = resultadoConsultaLectura.getString(1) // Nombre
                val descripcion = resultadoConsultaLectura.getString(2) // Descripcion

                if (id != null) {
                    usuarioEncontrado.id = id
                    usuarioEncontrado.nombre = nombre
                    usuarioEncontrado.descripcion = descripcion
                    /* Otra forma (con arreglo)
                    arregloUsuarios.add(
                        EUsuarioBDD(id, nombre, descripcion)
                    )*/
                }

            } while (resultadoConsultaLectura.moveToNext()) // para varios Usuarios
        }

        resultadoConsultaLectura.close()
        baseDatosLectura.close()

        return usuarioEncontrado


    }

    fun eliminarUsuarioFormulario(id: Int): Boolean {
        val conexionEscritura = writableDatabase
        val resultadoEliminacion = conexionEscritura.delete(
            "USUARIO",
            "id=?", // Evitar inyeccion SQL
            arrayOf(
                id.toString()
            )
        )
        conexionEscritura.close()
        return resultadoEliminacion.toInt() != -1
    }

    fun actualizarUsuarioFormulario(
        nombre: String,
        descripcion: String,
        idActualizar: Int
    ): Boolean {
        val conexionEscritura = writableDatabase

        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("nombre", nombre)
        valoresAActualizar.put("descripcion", descripcion)

        val resultadoActualizacion = conexionEscritura.update(
            "USUARIO",  // Nombre de la tabla
            valoresAActualizar, // Valores a actualizar
            "id=?", // Clausula WHERE
            arrayOf(
                idActualizar.toString()
            )   // Parametros clausula WHERE
        )

        conexionEscritura.close()
        return resultadoActualizacion != -1
    }

}
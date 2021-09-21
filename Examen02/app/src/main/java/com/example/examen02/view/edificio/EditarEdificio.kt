package com.example.examen02.view.edificio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.examen02.MainActivity
import com.example.examen02.R
import com.example.examen02.entities.Edificio
import com.example.examen02.persistence.FirestoreDB

class EditarEdificio : AppCompatActivity() {

    // Campos
    lateinit var txtNombre: EditText
    lateinit var txtNumPisos: EditText
    lateinit var txtArea: EditText
    lateinit var txtFecha: EditText
    lateinit var txtDireccion: EditText
    // Intent
    lateinit var edificio: Edificio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_edificio)

        // Edificio recibido
        edificio = intent.getParcelableExtra("edificio")!!

        // Campos
        txtNombre = findViewById(R.id.txt_nuevoNombre)
        txtNumPisos = findViewById(R.id.txt_nuevoNumPisos)
        txtArea = findViewById(R.id.txt_nuevaArea)
        txtFecha = findViewById(R.id.txt_nuevaFecha)
        txtDireccion = findViewById(R.id.txt_nuevaDireccion)

        // Se rellenan los campos
        txtNombre.setText(edificio?.nombre)
        txtNumPisos.setText(edificio?.numeroPisos.toString())
        txtArea.setText(edificio?.areaM2.toString())
        val fecha = edificio?.fechaApertura
        txtFecha.setText(FirestoreDB.sdf.format(fecha))
        txtDireccion.setText(edificio?.direccion)

        val btnActualizar = findViewById<Button>(R.id.btn_actualizarEdificio)
        btnActualizar.setOnClickListener {
            // Valores
            val nombre = txtNombre.text.toString()
            val numPisos = txtNumPisos.text.toString().toInt()
            val area = txtArea.text.toString().toFloat()
            val fechaString = txtFecha.text.toString()
            val direccion = txtDireccion.text.toString()

            // Actualizacion
            actualizarEdificio(
                Edificio(edificio!!.id!!, nombre, numPisos, area, FirestoreDB.sdf.parse(fechaString), direccion)
            )
        }

        val btnRegresar = findViewById<Button>(R.id.btn_regresarEditar)
        btnRegresar.setOnClickListener { abrirActividad(MainActivity::class.java) }

    }

    fun actualizarEdificio(nuevoEdificio: Edificio) {
        var mensaje = ""
        FirestoreDB.db.runTransaction { transaction ->
            transaction.update(
                FirestoreDB.coleccionEdificio.document(edificio!!.id!!),
                mapOf(
                    "nombre" to nuevoEdificio.nombre,
                    "numeroPisos" to nuevoEdificio.numeroPisos,
                    "areaM2" to nuevoEdificio.areaM2,
                    "fechaApertura" to nuevoEdificio.fechaApertura,
                    "direccion" to nuevoEdificio.direccion
                )
            )
        }
            // Mensaje de retroalimentacion
            .addOnSuccessListener {
                mensaje = "Se ha actualizado correctamente!"
            }
            .addOnFailureListener {
                mensaje = "Ha habido un error en la actualizacion"
            }
            .addOnCompleteListener {
                val msj = Toast.makeText(this, mensaje, Toast.LENGTH_SHORT)
                msj.show()
            }
    }

    fun abrirActividad(clase: Class<*>) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }
}
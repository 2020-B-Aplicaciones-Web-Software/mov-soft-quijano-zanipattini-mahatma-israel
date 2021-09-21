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
import com.google.firebase.Timestamp

class CrearEdificio : AppCompatActivity() {

    lateinit var txtNombre: EditText
    lateinit var txtNumPisos: EditText
    lateinit var txtArea: EditText
    lateinit var txtFecha: EditText
    lateinit var txtDireccion: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_edificio)

        val btnCrear = findViewById<Button>(R.id.btn_crearEdificio)
        btnCrear.setOnClickListener {
            // Campos
            txtNombre = findViewById(R.id.txt_nombre)
            txtNumPisos = findViewById(R.id.txt_numeroPisos)
            txtArea = findViewById(R.id.txt_area)
            txtFecha = findViewById(R.id.txt_fecha)
            txtDireccion = findViewById(R.id.txt_direccion)

            // Valores
            val nombre = txtNombre.text.toString()
            val numPisos = txtNumPisos.text.toString().toInt()
            val area = txtArea.text.toString().toFloat()
            val fechaString = txtFecha.text.toString()
            val fecha = FirestoreDB.sdf.parse(fechaString)
            val direccion = txtDireccion.text.toString()

            // Creacion
            crearEdificio(Edificio(
                null, nombre, numPisos, area, fecha, direccion
            ))
        }

        val btnRegresar = findViewById<Button>(R.id.btn_regresarCrear)
        btnRegresar.setOnClickListener { abrirActividad(MainActivity::class.java) }
        // TODO Bloquear boton de CREAR si la operacion demora mucho
    }

    // Create
    fun crearEdificio(edificio: Edificio) {
        val docId = FirestoreDB.coleccionEdificio.document().id
        var mensaje = ""
        FirestoreDB.coleccionEdificio
            .document(docId)
            .set(mapOf(
                "id" to docId,
                "nombre" to edificio.nombre,
                "numeroPisos" to edificio.numeroPisos,
                "areaM2" to edificio.areaM2,
                "fechaApertura" to Timestamp(edificio.fechaApertura!!),
                "direccion" to edificio.direccion
            ))
            // Mensaje de retroalimentacion
            .addOnSuccessListener {
                mensaje = "Se ha creado exitosamente!"
                // Se vacian los campos
                txtNombre.setText("")
                txtNumPisos.setText("")
                txtArea.setText("")
                txtFecha.setText("")
                txtDireccion.setText("")
            }
            .addOnFailureListener {
                mensaje = "Ha habido un error en la creacion"
            }
            .addOnCompleteListener {
                // TODO Desbloquear boton de CREAR
                val msj = Toast.makeText(this, mensaje, Toast.LENGTH_SHORT)
                msj.show()
            }
    }

    fun abrirActividad(clase: Class<*>) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }

}
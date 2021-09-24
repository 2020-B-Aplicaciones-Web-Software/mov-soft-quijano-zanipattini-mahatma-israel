package com.example.examen02.view.departamento

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.examen02.R
import com.example.examen02.entities.Departamento
import com.example.examen02.entities.Edificio
import com.example.examen02.persistence.FirestoreDB

class CrearDepartamento : AppCompatActivity() {

    val CODIGO_RESPUESTA_INTENT_EXPLICITO = 401

    lateinit var txtNombre: EditText
    lateinit var txtNumHabitaciones: EditText
    lateinit var txtNumBanos: EditText
    lateinit var txtArea: EditText
    lateinit var txtValor: EditText
    lateinit var txtLatitud: EditText
    lateinit var txtLongitud: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_departamento)

        // Edificio recibido
        val edificio = intent.getParcelableExtra<Edificio>("edificio")

        val btnCrear = findViewById<Button>(R.id.btn_crearDepartamento)
        btnCrear.setOnClickListener {
            // Campos
            txtNombre = findViewById(R.id.txt_nombreDpto)
            txtNumHabitaciones = findViewById(R.id.txt_numHab)
            txtNumBanos = findViewById(R.id.txt_numBan)
            txtArea = findViewById(R.id.txt_areaDpto)
            txtValor = findViewById(R.id.txt_valorDpto)
            txtLatitud = findViewById(R.id.txt_latitud)
            txtLongitud = findViewById(R.id.txt_Longitud)

            // Valores
            val nombre = txtNombre.text.toString()
            val numHab = txtNumHabitaciones.text.toString().toInt()
            val numBanos = txtNumBanos.text.toString().toInt()
            val area = txtArea.text.toString().toFloat()
            val valor = txtValor.text.toString().toFloat()
            val latitud = txtLatitud.text.toString().toFloat()
            val longitud = txtLongitud.text.toString().toFloat()

            // Creacion
            crearDepartamento(
                Departamento(null, nombre, numHab, numBanos, area, valor, latitud, longitud),
                edificio!!.id!!
            )

        }

        val btnRegresar = findViewById<Button>(R.id.btn_regresarDepartamento)
        btnRegresar.setOnClickListener { abrirActividadConParametros(ListarDepartamentos::class.java, edificio!!) }

    }

    // Create
    fun crearDepartamento(departamento: Departamento, edificioID: String) {
        val docId = FirestoreDB.coleccionEdificio.document().id
        var mensaje = ""
        FirestoreDB.subcoleccionDepartamento(edificioID)
            .document(docId)
            .set(mapOf(
                "id" to docId,
                "nombre" to departamento.nombre,
                "numeroHabitaciones" to departamento.numeroHabitaciones,
                "numeroBanos" to departamento.numeroBanos,
                "areaM2" to departamento.areaM2,
                "valor" to departamento.valor,
                "latitud" to departamento.latitud,
                "longitud" to departamento.longitud,
            ))
            // Mensaje de retroalimentacion
            .addOnSuccessListener {
                mensaje = "Se ha creado exitosamente!"
                // Se vacian los campos
                txtNombre.setText("")
                txtNumHabitaciones.setText("")
                txtNumBanos.setText("")
                txtArea.setText("")
                txtValor.setText("")
                txtLatitud.setText("")
                txtLongitud.setText("")
            }
            .addOnFailureListener {
                mensaje = "Ha habido un error en la creacion"
            }
            .addOnCompleteListener {
                val msj = Toast.makeText(this, mensaje, Toast.LENGTH_SHORT)
                msj.show()
            }
    }

    fun abrirActividadConParametros(clase: Class<*>, edificio: Edificio) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("edificio", edificio)
        startActivityForResult(intentExplicito, CODIGO_RESPUESTA_INTENT_EXPLICITO)
    }

}
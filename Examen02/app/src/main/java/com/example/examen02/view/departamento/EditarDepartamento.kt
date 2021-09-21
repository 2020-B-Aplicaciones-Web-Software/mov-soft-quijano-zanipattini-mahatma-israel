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

class EditarDepartamento : AppCompatActivity() {

    // Intent
    val CODIGO_RESPUESTA_INTENT_EXPLICITO = 401
    lateinit var dpto: Departamento
    lateinit var edificioID: String
    // Campos
    lateinit var txtNombre: EditText
    lateinit var txtNumHabitaciones: EditText
    lateinit var txtNumBanos: EditText
    lateinit var txtArea: EditText
    lateinit var txtValor: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_departamento)

        // Departamento recibido
        dpto = intent.getParcelableExtra<Departamento>("departamento")!!
        // ID edificio
        edificioID = intent.getStringExtra("edificioID")!!

        // Campos
        txtNombre = findViewById(R.id.txt_nuevoNombreDpto)
        txtNumHabitaciones = findViewById(R.id.txt_nuevoNumHab)
        txtNumBanos = findViewById(R.id.txt_nuevoNumBan)
        txtArea = findViewById(R.id.txt_nuevaAreaDpto)
        txtValor = findViewById(R.id.txt_nuevoValorDpto)

        // Se rellenan los campos
        txtNombre.setText(dpto?.nombre)
        txtNumHabitaciones.setText(dpto?.numeroHabitaciones.toString())
        txtNumBanos.setText(dpto?.numeroBanos.toString())
        txtArea.setText(dpto?.areaM2.toString())
        txtValor.setText(dpto?.valor.toString())

        val btnActualizar = findViewById<Button>(R.id.btn_editarDepartamento)
        btnActualizar.setOnClickListener {
            // Valores
            val nombre = txtNombre.text.toString()
            val numHab = txtNumHabitaciones.text.toString().toInt()
            val numBanos = txtNumBanos.text.toString().toInt()
            val area = txtArea.text.toString().toFloat()
            val valor = txtValor.text.toString().toFloat()

            // Actualizacion
            actualizarDepartamento(
                Departamento(dpto!!.id!!, nombre, numHab, numBanos, area, valor)
            )
        }

        val btnRegresar = findViewById<Button>(R.id.btn_regresarEditarDpto)
        btnRegresar.setOnClickListener {
            abrirActividadConParametros(ListarDepartamentos::class.java, dpto!!.edificio!!)
        }
    }

    // Update
    fun actualizarDepartamento(departamento: Departamento) {
        var mensaje = ""
        FirestoreDB.db.runTransaction { transaction ->
            transaction.update(
                FirestoreDB.subcoleccionDepartamento(edificioID).document(departamento!!.id!!),
                mapOf(
                    "nombre" to departamento.nombre,
                    "numeroHabitaciones" to departamento.numeroHabitaciones,
                    "numeroBanos" to departamento.numeroBanos,
                    "areaM2" to departamento.areaM2,
                    "valor" to departamento.valor
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

    fun abrirActividadConParametros(clase: Class<*>, edificio: Edificio) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("edificio", edificio)
        startActivityForResult(intentExplicito, CODIGO_RESPUESTA_INTENT_EXPLICITO)
    }

}
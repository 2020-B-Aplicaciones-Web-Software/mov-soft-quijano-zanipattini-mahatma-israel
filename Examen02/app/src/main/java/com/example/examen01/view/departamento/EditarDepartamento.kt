package com.example.examen01.view.departamento

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.examen01.R
import com.example.examen01.entities.Departamento
import com.example.examen01.entities.Edificio
import com.example.examen01.sqlitehelper.SQLiteHelper

class EditarDepartamento : AppCompatActivity() {

    val helper = SQLiteHelper(this)
    val CODIGO_RESPUESTA_INTENT_EXPLICITO = 401

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_departamento)

        // Departamento recibido
        val dpto = intent.getParcelableExtra<Departamento>("departamento")

        // Campos
        val txtNombre = findViewById<EditText>(R.id.txt_nuevoNombreDpto)
        val txtNumHabitaciones = findViewById<EditText>(R.id.txt_nuevoNumHab)
        val txtNumBanos = findViewById<EditText>(R.id.txt_nuevoNumBan)
        val txtArea = findViewById<EditText>(R.id.txt_nuevaAreaDpto)
        val txtValor = findViewById<EditText>(R.id.txt_nuevoValorDpto)

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
            val result = helper.actualizarDepartamento(dpto?.id!!, nombre, numHab, numBanos, area, valor)

            // Mensaje de retroalimentacion
            var mensaje: String
            if (result)
                mensaje = "Se ha actualizado correctamente!"
            else
                mensaje = "Ha habido un error en la actualizacion"
            val msj = Toast.makeText(this, mensaje, Toast.LENGTH_SHORT)
            msj.show()

        }

        val btnRegresar = findViewById<Button>(R.id.btn_regresarEditarDpto)
        btnRegresar.setOnClickListener {
            abrirActividadConParametros(ListarDepartamentos::class.java, dpto?.edificio!!)
        }


    }

    fun abrirActividadConParametros(clase: Class<*>, edificio: Edificio) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("edificio", edificio)
        startActivityForResult(intentExplicito, CODIGO_RESPUESTA_INTENT_EXPLICITO)
    }

}
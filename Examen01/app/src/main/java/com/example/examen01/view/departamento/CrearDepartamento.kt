package com.example.examen01.view.departamento

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.examen01.R
import com.example.examen01.entities.Edificio
import com.example.examen01.sqlitehelper.SQLiteHelper

class CrearDepartamento : AppCompatActivity() {

    val departamentoSQL = SQLiteHelper(this)
    val CODIGO_RESPUESTA_INTENT_EXPLICITO = 401

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_departamento)

        // Edificio recibido
        val edificio = intent.getParcelableExtra<Edificio>("edificio")

        val btnCrear = findViewById<Button>(R.id.btn_crearDepartamento)
        btnCrear.setOnClickListener {
            // Campos
            val txtNombre = findViewById<EditText>(R.id.txt_nombreDpto)
            val txtNumHabitaciones = findViewById<EditText>(R.id.txt_numHab)
            val txtNumBanos = findViewById<EditText>(R.id.txt_numBan)
            val txtArea = findViewById<EditText>(R.id.txt_areaDpto)
            val txtValor = findViewById<EditText>(R.id.txt_valorDpto)

            // Valores
            val nombre = txtNombre.text.toString()
            val numHab = txtNumHabitaciones.text.toString().toInt()
            val numBanos = txtNumBanos.text.toString().toInt()
            val area = txtArea.text.toString().toFloat()
            val valor = txtValor.text.toString().toFloat()

            // Creacion
            val result = departamentoSQL.crearDepartamento(nombre, numHab, numBanos, area, valor, edificio?.id!!)

            // Mensaje de retroalimentacion
            var mensaje: String
            if (result) {
                mensaje = "Se ha creado exitosamente!"
                // Se vacian los campos
                txtNombre.setText("")
                txtNumHabitaciones.setText("")
                txtNumBanos.setText("")
                txtArea.setText("")
                txtValor.setText("")
            } else
                mensaje = "Ha habido un error en la creacion"
            val msj = Toast.makeText(this, mensaje, Toast.LENGTH_SHORT)
            msj.show()
        }

        val btnRegresar = findViewById<Button>(R.id.btn_regresarDepartamento)
        btnRegresar.setOnClickListener { abrirActividadConParametros(ListarDepartamentos::class.java, edificio!!) }

    }

    fun abrirActividadConParametros(clase: Class<*>, edificio: Edificio) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("edificio", edificio)
        startActivityForResult(intentExplicito, CODIGO_RESPUESTA_INTENT_EXPLICITO)
    }

}
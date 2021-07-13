package com.example.trabajoenclase01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class AgregarEdificio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_edificio)

        val helper = ESqliteHelperEdificio(this)

        val regresar = findViewById<Button>(R.id.btn_regresar)
        regresar.setOnClickListener { abrirActividad(MainActivity::class.java) }

        val crearEdificio = findViewById<Button>(R.id.btn_crear)
        crearEdificio.setOnClickListener {
            // Campos
            val nombreField = findViewById<EditText>(R.id.text_nombre)
            val numeroPisosField = findViewById<EditText>(R.id.text_numeroPisos)

            // Atributos
            val nombre = nombreField.text.toString()
            val numeroPisos = numeroPisosField.text.toString().toInt()

            // Escriture
            val resultado = helper.crearEdificio(nombre, numeroPisos)

            // Vaciar campos de entrada
            nombreField.setText("")
            numeroPisosField.setText("")
        }

    }

    fun abrirActividad(clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        startActivity(intentExplicito)
    }
}
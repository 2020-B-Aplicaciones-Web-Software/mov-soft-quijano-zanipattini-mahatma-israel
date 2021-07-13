package com.example.trabajoenclase01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class ActualizarEdificio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_edificio)

        // Objeto
        val edificio = intent.getParcelableExtra<Edificio>("edificio")
        // Campos
        val nombre = findViewById<EditText>(R.id.text_nuevoNombre)
        val numPisos = findViewById<EditText>(R.id.text_nuevoNumPisos)
        nombre.setText(edificio?.nombre)
        numPisos.setText(edificio?.numeroPisos!!.toString())

        val helper = ESqliteHelperEdificio(this)

        val actualizar = findViewById<Button>(R.id.btn_actualizar)
        actualizar.setOnClickListener {
            // Atributos
            val nuevoNombre = nombre.text.toString()
            val nuevoNumPisos = numPisos.text.toString().toInt()

            // Escriture
            val resultado = helper.actualizarEdificio(edificio?.id, nuevoNombre, nuevoNumPisos)

            // Regresar a la actividad principal
            val intentExplicito = Intent(this, MainActivity::class.java)
            startActivity(intentExplicito)
        }
    }
}
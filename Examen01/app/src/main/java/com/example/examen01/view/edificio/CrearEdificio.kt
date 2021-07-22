package com.example.examen01.view.edificio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.examen01.MainActivity
import com.example.examen01.R
import com.example.examen01.sqlitehelper.SQLiteHelper
import java.text.SimpleDateFormat

class CrearEdificio : AppCompatActivity() {

    val edificioSQL = SQLiteHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_edificio)

        val btnCrear = findViewById<Button>(R.id.btn_crearEdificio)
        btnCrear.setOnClickListener {
            // TODO Poner un if (fecha cumple con el formato dado)
            //  else (despliega mensaje que indica que debe poner bien)
            // Campos
            val txtNombre = findViewById<EditText>(R.id.txt_nombre)
            val txtNumPisos = findViewById<EditText>(R.id.txt_numeroPisos)
            val txtArea = findViewById<EditText>(R.id.txt_area)
            val txtFecha = findViewById<EditText>(R.id.txt_fecha)
            val txtDireccion = findViewById<EditText>(R.id.txt_direccion)

            // Valores
            val nombre = txtNombre.text.toString()
            val numPisos = txtNumPisos.text.toString().toInt()
            val area = txtArea.text.toString().toFloat()
            val fechaString = txtFecha.text.toString()
            val direccion = txtDireccion.text.toString()

            // Creacion
            val result = edificioSQL.crearEdificio(nombre, numPisos, area, fechaString, direccion)

            // Mensaje de retroalimentacion
            var mensaje: String
            if (result) {
                mensaje = "Se ha creado exitosamente!"
                // Se vacian los campos
                txtNombre.setText("")
                txtNumPisos.setText("")
                txtArea.setText("")
                txtFecha.setText("")
                txtDireccion.setText("")
            } else
                mensaje = "Ha habido un error en la creacion"
            val msj = Toast.makeText(this, mensaje, Toast.LENGTH_SHORT)
            msj.show()

        }

        val btnRegresar = findViewById<Button>(R.id.btn_regresarCrear)
        btnRegresar.setOnClickListener { abrirActividad(MainActivity::class.java) }
    }

    fun abrirActividad(clase: Class<*>) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }

}
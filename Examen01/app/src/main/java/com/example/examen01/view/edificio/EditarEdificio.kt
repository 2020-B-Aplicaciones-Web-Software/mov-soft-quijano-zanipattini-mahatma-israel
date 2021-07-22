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
import com.example.examen01.entities.Edificio
import com.example.examen01.sqlitehelper.SQLiteHelper
import java.text.SimpleDateFormat

class EditarEdificio : AppCompatActivity() {

    val helper = SQLiteHelper(this)
    val sdf = SimpleDateFormat("dd-MM-yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_edificio)

        // Edificio recibido
        val edificio = intent.getParcelableExtra<Edificio>("edificio")

        // Campos
        val txtNombre = findViewById<EditText>(R.id.txt_nuevoNombre)
        val txtNumPisos = findViewById<EditText>(R.id.txt_nuevoNumPisos)
        val txtArea = findViewById<EditText>(R.id.txt_nuevaArea)
        val txtFecha = findViewById<EditText>(R.id.txt_nuevaFecha)
        val txtDireccion = findViewById<EditText>(R.id.txt_nuevaDireccion)

        // Se rellenan los campos
        txtNombre.setText(edificio?.nombre)
        txtNumPisos.setText(edificio?.numeroPisos.toString())
        txtArea.setText(edificio?.areaM2.toString())
        val fecha = edificio?.fechaApertura
        txtFecha.setText(sdf.format(fecha))
        txtDireccion.setText(edificio?.direccion)

        val btnActualizar = findViewById<Button>(R.id.btn_actualizarEdificio)
        btnActualizar.setOnClickListener {
            // TODO Poner un if (fecha cumple con el formato dado)
            //  else (despliega mensaje que indica que debe poner bien)

            // Valores
            val nombre = txtNombre.text.toString()
            val numPisos = txtNumPisos.text.toString().toInt()
            val area = txtArea.text.toString().toFloat()
            val fechaString = txtFecha.text.toString()
            val direccion = txtDireccion.text.toString()

            // Actualizacion
            val result = helper.actualizarEdificio(edificio?.id!!, nombre, numPisos, area, fechaString, direccion)

            // Mensaje de retroalimentacion
            var mensaje: String
            if (result)
                mensaje = "Se ha actualizado correctamente!"
            else
                mensaje = "Ha habido un error en la actualizacion"
            val msj = Toast.makeText(this, mensaje, Toast.LENGTH_SHORT)
            msj.show()
        }

        val btnRegresar = findViewById<Button>(R.id.btn_regresarEditar)
        btnRegresar.setOnClickListener { abrirActividad(MainActivity::class.java) }

    }

    fun abrirActividad(clase: Class<*>) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }
}
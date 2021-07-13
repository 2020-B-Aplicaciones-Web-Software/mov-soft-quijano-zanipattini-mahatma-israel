package com.example.trabajoenclase01

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get

class MainActivity : AppCompatActivity() {
    val helper = ESqliteHelperEdificio(this)
    var posicionItemSeleccionado = 0
    val CODIGO_RESPUESTA_INTENT_EXPLICITO = 401

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lista = findViewById<ListView>(R.id.list_bdd)
        cargarDatos()
        registerForContextMenu(lista)

        val botonCrear = findViewById<Button>(R.id.btn_pantallaCreacion)
        botonCrear.setOnClickListener{ abrirActividad(AgregarEdificio::class.java) }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val id = info.position
        posicionItemSeleccionado = id
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        // Se obtiene el Edificio seleccionado
        val edificioSeleccionado = helper.leerEdificios()[posicionItemSeleccionado]
        return when (item?.itemId) {
            // Actualizar
            R.id.menu_actualizar -> {
                abrirActividadConParametros(ActualizarEdificio::class.java, edificioSeleccionado)
                return true
            }
            // Eliminar
            R.id.menu_eliminar -> {
                helper.eliminarEdificioPorID(edificioSeleccionado.id)
                cargarDatos()
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    fun abrirActividad(clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        startActivity(intentExplicito)
    }

    fun abrirActividadConParametros(clase: Class<*>, edificio: Edificio) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("edificio", edificio)
        startActivityForResult(intentExplicito, CODIGO_RESPUESTA_INTENT_EXPLICITO)
    }

    fun cargarDatos() {
        val lista = findViewById<ListView>(R.id.list_bdd)
        lista.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            helper.leerEdificios()
        )
    }
}
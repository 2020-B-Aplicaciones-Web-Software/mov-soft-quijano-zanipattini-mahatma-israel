package com.example.examen01

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.examen01.entities.Edificio
import com.example.examen01.sqlitehelper.SQLiteHelper
import com.example.examen01.view.departamento.ListarDepartamentos
import com.example.examen01.view.edificio.CrearEdificio
import com.example.examen01.view.edificio.EditarEdificio

class MainActivity : AppCompatActivity() {

    val helper = SQLiteHelper(this)
    var itemIndex = 0
    val CODIGO_RESPUESTA_INTENT_EXPLICITO = 401

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCrearEdificio = findViewById<Button>(R.id.btn_pantallaCrear)
        btnCrearEdificio.setOnClickListener {
            abrirActividad(CrearEdificio::class.java)
        }

        val txtMsj = findViewById<TextView>(R.id.tv_msjEdificio)
        txtMsj.setVisibility(View.GONE)

        val listaEdificios = findViewById<ListView>(R.id.lv_edificios)
        cargarDatos(listaEdificios)
        registerForContextMenu(listaEdificios)

    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        menuInflater.inflate(R.menu.menu_edificio, menu)

        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        itemIndex = info.position
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        // Se obtiene el edificio seleccionado
        val edificio = helper.leerEdificios()[itemIndex]
        return when (item?.itemId) {
            // Ver departamentos
            R.id.menu_departamentos -> {
                abrirActividadConParametros(ListarDepartamentos::class.java, edificio)
                return true
            }
            // Editar edificio
            R.id.menu_editar -> {
                abrirActividadConParametros(EditarEdificio::class.java, edificio)
                return true
            }
            // Eliminar edificio
            R.id.menu_eliminar -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Eliminar Edificio")
                builder.setMessage("Esta seguro de querer eliminar el edificio ${edificio.nombre}")

                builder.setPositiveButton(
                    "Si",
                    DialogInterface.OnClickListener{dialog, which ->
                        helper.eliminarEdificioPorID(edificio.id)
                        cargarDatos(findViewById<ListView>(R.id.lv_edificios))
                    }
                )

                builder.setNegativeButton("No", null)
                val dialogo = builder.create()
                dialogo.show()

                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    fun abrirActividad(clase: Class<*>) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }

    fun abrirActividadConParametros(clase: Class<*>, edificio: Edificio) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("edificio", edificio)
        startActivityForResult(intentExplicito, CODIGO_RESPUESTA_INTENT_EXPLICITO)
    }

    fun cargarDatos(lista: ListView) {
        val edificios = helper.leerEdificios()
        lista.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            edificios
        )
        if (edificios.size == 0) {
            val txtMsj = findViewById<TextView>(R.id.tv_msjEdificio)
            txtMsj.setVisibility(View.VISIBLE)
            txtMsj.setText("No hay edificios registrados")
        }
    }
}
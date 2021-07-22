package com.example.examen01.view.departamento

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.examen01.MainActivity
import com.example.examen01.R
import com.example.examen01.entities.Departamento
import com.example.examen01.entities.Edificio
import com.example.examen01.sqlitehelper.SQLiteHelper

class ListarDepartamentos : AppCompatActivity() {

    val helper = SQLiteHelper(this)
    val CODIGO_RESPUESTA_INTENT_EXPLICITO = 401
    var itemIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_departamentos)

        // Edificio recibido
        val edificio = intent.getParcelableExtra<Edificio>("edificio")

        val nombreEdificio = findViewById<TextView>(R.id.tv_nombreEdificio)
        nombreEdificio.setText(edificio!!.nombre)

        val btnCrear = findViewById<Button>(R.id.btn_pantallaCrearDepartamento)
        btnCrear.setOnClickListener {
            abrirActividadConParametros(CrearDepartamento::class.java, edificio)
        }

        val txtMsj = findViewById<TextView>(R.id.tv_msjDpto)
        txtMsj.setVisibility(View.GONE)

        val listaDepartamentos = findViewById<ListView>(R.id.lv_departamentos)
        cargarDatos(listaDepartamentos, edificio)
        registerForContextMenu(listaDepartamentos)

        val btnRegresar = findViewById<Button>(R.id.btn_regresarInicio)
        btnRegresar.setOnClickListener { abrirActividad(MainActivity::class.java) }

    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        menuInflater.inflate(R.menu.menu_departamento, menu)

        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        itemIndex = info.position
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val edificio = intent.getParcelableExtra<Edificio>("edificio")
        // Se obtiene el departamento seleccionado
        val departamento = helper.leerDepartamentos(edificio!!)[itemIndex]
        return when (item?.itemId) {
            // Editar departamento
            R.id.menu_editarDpto -> {
                abrirActividadConParametros(EditarDepartamento::class.java, departamento)
                return true
            }
            // Eliminar edificio
            R.id.menu_eliminarDpto -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Eliminar Departamento")
                builder.setMessage("Esta seguro de querer eliminar el departamento ${departamento.nombre}")

                builder.setPositiveButton(
                    "Si",
                    DialogInterface.OnClickListener{ dialog, which ->
                        helper.eliminarDepartamentoPorID(departamento.id)
                        cargarDatos(findViewById<ListView>(R.id.lv_departamentos), edificio)
                        val msj = Toast.makeText(this, "Registro eliminado", Toast.LENGTH_SHORT)
                        msj.show()
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

    fun abrirActividadConParametros(clase: Class<*>, objeto: Any) {
        val intentExplicito = Intent(this, clase)
        if (objeto is Edificio)
            intentExplicito.putExtra("edificio", objeto)
        else if (objeto is Departamento)
            intentExplicito.putExtra("departamento", objeto)
        startActivityForResult(intentExplicito, CODIGO_RESPUESTA_INTENT_EXPLICITO)
    }

    fun cargarDatos(lista: ListView, edificio: Edificio) {
        val departamentos = helper.leerDepartamentos(edificio)
        lista.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            departamentos
        )
        if (departamentos.size == 0) {
            val txtMsj = findViewById<TextView>(R.id.tv_msjDpto)
            txtMsj.setVisibility(View.VISIBLE)
            txtMsj.setText("No hay departamentos registrados para el edificio ${edificio.nombre}")
        }
    }

}
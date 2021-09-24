package com.example.examen02.view.departamento

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.examen02.MainActivity
import com.example.examen02.R
import com.example.examen02.entities.Departamento
import com.example.examen02.entities.Edificio
import com.example.examen02.persistence.FirestoreDB

class ListarDepartamentos : AppCompatActivity() {

    // Departamentos
    val listaDepartamentos: MutableList<Departamento> = ArrayList()
    var itemIndex = 0
    // Intent
    val CODIGO_RESPUESTA_INTENT_EXPLICITO = 401
    lateinit var edificio: Edificio
    // View
    lateinit var listViewDepartamentos: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_departamentos)

        // Edificio recibido
        edificio = intent.getParcelableExtra<Edificio>("edificio")!!

        val nombreEdificio = findViewById<TextView>(R.id.tv_nombreEdificio)
        nombreEdificio.setText(edificio!!.nombre)

        val btnCrear = findViewById<Button>(R.id.btn_pantallaCrearDepartamento)
        btnCrear.setOnClickListener {
            abrirActividadConParametros(CrearDepartamento::class.java, edificio)
        }

        val txtMsj = findViewById<TextView>(R.id.tv_msjDpto)
        txtMsj.setVisibility(View.GONE)

        listViewDepartamentos = findViewById<ListView>(R.id.lv_departamentos)
        leerDepartamentos(edificio!!.id!!)
        registerForContextMenu(listViewDepartamentos)

        val btnRegresar = findViewById<Button>(R.id.btn_regresarInicio)
        btnRegresar.setOnClickListener { abrirActividad(MainActivity::class.java) }

    }

    // Read
    fun leerDepartamentos(edificioID: String) {
        FirestoreDB.subcoleccionDepartamento(edificioID)
            .get()
            .addOnSuccessListener { documents ->
                documents.forEach { doc ->
                    val dpto = Departamento(
                        doc["id"].toString(),
                        doc["nombre"].toString(),
                        doc["numeroHabitaciones"].toString().toInt(),
                        doc["numeroBanos"].toString().toInt(),
                        doc["areaM2"].toString().toFloat(),
                        doc["valor"].toString().toFloat(),
                        doc["latitud"].toString().toFloat(),
                        doc["longitud"].toString().toFloat(),
                    )
                    dpto.edificio = edificio
                    listaDepartamentos.add(dpto)
                }
                actualizarListView()
            }
    }

    // Delete
    fun eliminarDepartamento(edificioID: String) {
        FirestoreDB.subcoleccionDepartamento(edificioID)
            .document(listaDepartamentos[itemIndex]!!.id!!)
            .delete()
            .addOnSuccessListener {
                listaDepartamentos.removeAt(itemIndex)
                actualizarListView()
                val msj = Toast.makeText(this, "Registro eliminado", Toast.LENGTH_SHORT)
                msj.show()
            }
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
        // Se obtiene el departamento seleccionado
        val departamento = listaDepartamentos[itemIndex]
        return when (item?.itemId) {
            // Editar departamento
            R.id.menu_editarDpto -> {
                abrirActividadConParametros(EditarDepartamento::class.java, departamento)
                return true
            }
            // Eliminar departamento
            R.id.menu_eliminarDpto -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Eliminar Departamento")
                builder.setMessage("Esta seguro de querer eliminar el departamento ${departamento.nombre}")

                builder.setPositiveButton("Si") { _, _ ->
                    eliminarDepartamento(edificio!!.id!!)
                }

                builder.setNegativeButton("No", null)
                val dialogo = builder.create()
                dialogo.show()
                return true
            }
            // Ver ubicacion (mapa)
            R.id.menu_mapa -> {
                abrirActividadConParametros(MapaDepartamento::class.java, departamento)
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
        else if (objeto is Departamento) {
            intentExplicito.putExtra("departamento", objeto)
            intentExplicito.putExtra("edificio", edificio)
        }
        startActivityForResult(intentExplicito, CODIGO_RESPUESTA_INTENT_EXPLICITO)
    }

    fun actualizarListView() {
        listViewDepartamentos.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            listaDepartamentos
        )
        if (listaDepartamentos.size == 0) {
            val txtMsj = findViewById<TextView>(R.id.tv_msjDpto)
            txtMsj.setVisibility(View.VISIBLE)
            txtMsj.setText("No hay departamentos registrados para el edificio ${edificio.nombre}")
        }
    }

}
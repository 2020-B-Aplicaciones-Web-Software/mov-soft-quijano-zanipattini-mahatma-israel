package com.example.examen02

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.examen02.entities.Edificio
import com.example.examen02.persistence.FirestoreDB
import com.example.examen02.view.departamento.ListarDepartamentos
import com.example.examen02.view.edificio.CrearEdificio
import com.example.examen02.view.edificio.EditarEdificio
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

class MainActivity : AppCompatActivity() {

    // Edificios
    val listaEdificios: MutableList<Edificio> = ArrayList()
    var itemIndex = 0
    // Intent
    val CODIGO_RESPUESTA_INTENT_EXPLICITO = 401
    // View
    lateinit var listViewEdificios: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCrearEdificio = findViewById<Button>(R.id.btn_pantallaCrear)
        btnCrearEdificio.setOnClickListener {
            abrirActividad(CrearEdificio::class.java)
        }

        val txtMsj = findViewById<TextView>(R.id.tv_msjEdificio)
        txtMsj.setVisibility(View.GONE)

        listViewEdificios = findViewById(R.id.lv_edificios)
        leerEdificios()
        registerForContextMenu(listViewEdificios)

    }

    // Read
    fun leerEdificios() {
        FirestoreDB.coleccionEdificio
            .get()
            .addOnSuccessListener { documents ->
                documents.forEach { doc ->
                    val fechaApertura = doc["fechaApertura"] as Timestamp
                    listaEdificios.add(Edificio(
                        doc["id"].toString(),
                        doc["nombre"].toString(),
                        doc["numeroPisos"].toString().toInt(),
                        doc["areaM2"].toString().toFloat(),
                        fechaApertura.toDate(),
                        doc["direccion"].toString()
                    ))
                }
                actualizarListView()
            }
    }

    // Delete
    fun eliminarEdificio(edificioID: String) {
        // Eliminar todos los Departamentos del Edificio
        val subcoleccion = FirestoreDB.subcoleccionDepartamento(edificioID)
        subcoleccion
            .get()
            .addOnSuccessListener { documents ->
                val departamentosID: MutableList<String> = ArrayList()
                documents.forEach { doc ->
                    departamentosID.add(doc["id"].toString())
                }
                departamentosID.forEach {
                    subcoleccion.document(it)
                        .delete()
                }
            }
            // Eliminar Edificio
            .addOnCompleteListener {
                FirestoreDB.coleccionEdificio
                    .document(edificioID)
                    .delete()
                    .addOnSuccessListener {
                        val msj = Toast.makeText(this, "Registro eliminado", Toast.LENGTH_SHORT)
                        msj.show()
                        actualizarListView()
                    }
            }
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
        val edificio = listaEdificios[itemIndex]
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

                builder.setPositiveButton("Si") { _, _ ->
                    eliminarEdificio(edificio!!.id!!)
                }

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

    fun actualizarListView() {
        listViewEdificios.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            listaEdificios
        )
        if (listaEdificios.size == 0) {
            val txtMsj = findViewById<TextView>(R.id.tv_msjEdificio)
            txtMsj.setVisibility(View.VISIBLE)
            txtMsj.setText("No hay edificios registrados")
        }
    }
}
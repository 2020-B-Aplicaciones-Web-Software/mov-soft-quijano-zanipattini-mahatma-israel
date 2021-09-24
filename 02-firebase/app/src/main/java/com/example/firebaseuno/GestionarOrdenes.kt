package com.example.firebaseuno

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.firebaseuno.entities.Orden
import com.example.firebaseuno.entities.Producto
import com.example.firebaseuno.entities.Restaurante
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.reflect.typeOf

class GestionarOrdenes : AppCompatActivity() {
    // Firestore
    val db = Firebase.firestore
    val restaurantesRef = db.collection("restaurante")
    val ordenesRef = db.collection("orden")
    // Listas
    val listaRestaurantes: MutableList<Restaurante> = ArrayList()
    val listaOrdenes: MutableList<Orden> = ArrayList()
    val listaDocumentosID: MutableList<DocumentReference> = ArrayList()
    // Menu contextual
    var indiceLista: Int = 0
    var estadoActual: String = Orden.POR_RECIBIR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestionar_ordenes)

        val listViewOrdenes = findViewById<ListView>(R.id.lv_ordenes)
        registerForContextMenu(listViewOrdenes)

        val menuEstados = findViewById<Spinner>(R.id.sp_estados)
        val adaptadorEstados = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item,
            Orden.ESTADOS
        )
        menuEstados.adapter = adaptadorEstados

        val menuRestaurantes = findViewById<Spinner>(R.id.sp_restaurantes_ordenes)
        restaurantesRef.get()
            .addOnSuccessListener { result ->
                result.forEach { listaRestaurantes.add(
                    Restaurante(
                        it.data["nombre"].toString(),
                        it.id
                    )
                ) }
                val adapterRestaurantes = ArrayAdapter(
                    this, android.R.layout.simple_spinner_dropdown_item, listaRestaurantes
                )
                menuRestaurantes.adapter = adapterRestaurantes

                obtenerOrdenes(menuEstados.selectedItem.toString(), menuRestaurantes.selectedItem.toString())
            }

        val botonFiltrar = findViewById<Button>(R.id.btn_filtrar)
        botonFiltrar.setOnClickListener {
            obtenerOrdenes(menuEstados.selectedItem.toString(), menuRestaurantes.selectedItem.toString())
        }

    }

    fun obtenerOrdenes(estado: String, restaurante: String) {
        listaOrdenes.clear()
        listaDocumentosID.clear()
        ordenesRef
            .whereEqualTo("estado", estado)
            .whereEqualTo("restaurante.nombre", restaurante)
            .get()
            .addOnSuccessListener { snapshot ->
                snapshot.forEach{
                    val productos = it.data["productos"] as ArrayList< HashMap<String, Any> >
                    val listaProductos = productos.map {
                        Producto(
                            it["nombre"].toString(),
                            it["precio"].toString().toFloat(),
                            it["cantidad"].toString().toInt(),
                            it["uid"].toString()
                        )
                    }
                    listaOrdenes.add(
                        Orden(
                            it.data["fechaPedido"].toString(),
                            it.data["total"].toString().toFloat(),
                            it.data["estado"].toString(),
                            it.data["usuario"].toString(),
                            ArrayList(listaProductos)
                        )
                    )
                    listaDocumentosID.add(ordenesRef.document(it.id))
                }
                actualizarOrdenes()
                estadoActual = estado
            }
            .addOnFailureListener {  }
    }

    fun actualizarOrdenes() {
        val listViewAdapter = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, listaOrdenes
        )
        val listViewOrdenes = findViewById<ListView>(R.id.lv_ordenes)
        listViewOrdenes.adapter = listViewAdapter
    }

    fun cambiarEstado(documento: DocumentReference, estado: String) {
        db.runTransaction { transaction ->
            transaction.update(documento, "estado", estado)
        }
    }

    // Menu Contextual
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        val inflater = menuInflater
        inflater.inflate(R.menu.menu_orden_estado, menu)

        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val id = info.position
        indiceLista = id

        // Ocultar y mostrar opciones correspondientes
        val opcionPreparando: MenuItem = menu!!.findItem(R.id.menu_estado_preparando)
        val opcionEnviado: MenuItem = menu!!.findItem(R.id.menu_estado_enviado)
        val opcionCancelado: MenuItem = menu!!.findItem(R.id.menu_estado_cancelado)
        if (estadoActual.equals(Orden.POR_RECIBIR)) {
            opcionPreparando.setVisible(true)
            opcionEnviado.setVisible(false)
        } else if (estadoActual.equals(Orden.PREPARANDO)) {
            opcionPreparando.setVisible(false)
            opcionEnviado.setVisible(true)
        } else {
            opcionPreparando.setVisible(false)
            opcionEnviado.setVisible(false)
            opcionCancelado.setVisible(false)
        }

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            // Preparando
            R.id.menu_estado_preparando -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Eliminar producto")
                builder.setMessage("¿Esta seguro que quiere cambiar el estado a PREPARANDO?")
                builder.setPositiveButton(
                    "Si",
                    DialogInterface.OnClickListener { dialog, which ->
                        cambiarEstado(listaDocumentosID[indiceLista], Orden.PREPARANDO)
                        listaOrdenes.removeAt(indiceLista)
                        listaDocumentosID.removeAt(indiceLista)
                        actualizarOrdenes()
                    }
                )
                builder.setNegativeButton("No", null)
                builder.create().show()
                return true
            }
            // Enviado
            R.id.menu_estado_enviado -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Eliminar producto")
                builder.setMessage("¿Esta seguro que quiere cambiar el estado a ENVIADO?")
                builder.setPositiveButton(
                    "Si",
                    DialogInterface.OnClickListener { dialog, which ->
                        cambiarEstado(listaDocumentosID[indiceLista], Orden.ENVIADO)
                        listaOrdenes.removeAt(indiceLista)
                        listaDocumentosID.removeAt(indiceLista)
                        actualizarOrdenes()
                    }
                )
                builder.setNegativeButton("No", null)
                builder.create().show()
                return true
            }
            // Cancelado
            R.id.menu_estado_cancelado -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Eliminar producto")
                builder.setMessage("¿Esta seguro que quiere cambiar el estado a CANCELADO?")
                builder.setPositiveButton(
                    "Si",
                    DialogInterface.OnClickListener { dialog, which ->
                        cambiarEstado(listaDocumentosID[indiceLista], Orden.CANCELADO)
                        listaOrdenes.removeAt(indiceLista)
                        listaDocumentosID.removeAt(indiceLista)
                        actualizarOrdenes()
                    }
                )
                builder.setNegativeButton("No", null)
                builder.create().show()
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

}
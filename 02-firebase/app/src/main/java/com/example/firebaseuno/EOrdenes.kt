package com.example.firebaseuno

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.firebaseuno.entities.ProductoOrden
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class EOrdenes : AppCompatActivity() {

    var posicionItemSeleccionado = 0
    // ProductoOrden
    val productosAgregados: MutableList<ProductoOrden> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eordenes)

        val listaProductosAgregados = findViewById<ListView>(R.id.lv_lista_productos)
        registerForContextMenu(listaProductosAgregados)

        val db = Firebase.firestore
        val referenciaRestaurantes = db.collection("restaurante")
        val referenciaProductos = db.collection("producto")

        // Restaurantes
        val listaRestaurantes: MutableList<String> = ArrayList()
        val menuRestaurantes = findViewById<View>(R.id.sp_restaurantes) as Spinner
        referenciaRestaurantes.get()
            .addOnSuccessListener { result ->
                result.forEach { listaRestaurantes.add(it.data["nombre"].toString()) }
                val adapterRestaurantes = ArrayAdapter(
                    this, android.R.layout.simple_spinner_item, listaRestaurantes
                )
                adapterRestaurantes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                menuRestaurantes.adapter = adapterRestaurantes
            }

        // Productos
        val listaProductos: MutableList<String> = ArrayList()
        val menuProductos = findViewById<View>(R.id.sp_productos) as Spinner
        referenciaProductos.get()
            .addOnSuccessListener { result ->
                result.forEach { listaProductos.add(it.data["nombre"].toString()) }
                val adapterProductos = ArrayAdapter(
                    this, android.R.layout.simple_spinner_item, listaProductos
                )
                adapterProductos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                menuProductos.adapter = adapterProductos
            }

        val botonAdd = findViewById<Button>(R.id.btn_anadir_lista_producto)
        botonAdd.setOnClickListener { agregarProducto(productosAgregados) }

        val botonCompletarPedido = findViewById<Button>(R.id.btn_completar_pedido)
        botonCompletarPedido.setOnClickListener{ crearOrden() }
    }

    fun agregarProducto(productosAgregados: MutableList<ProductoOrden>) {
        val menuRestaurantes = findViewById<View>(R.id.sp_restaurantes) as Spinner
        val menuProductos = findViewById<View>(R.id.sp_productos) as Spinner
        val cantidadText = findViewById<EditText>(R.id.et_cantidad_producto)

        val db = Firebase.firestore
        val referenciaProductos = db.collection("producto")
        var precioUnitario: Float = 0.0f
        referenciaProductos
            .whereEqualTo("nombre", menuProductos.selectedItem.toString())
            .get()
            .addOnSuccessListener { docs ->
                docs.forEach { precioUnitario = it.data["precio"].toString().toFloat() }
                productosAgregados.add(
                    ProductoOrden(
                        menuRestaurantes.selectedItem.toString(),
                        menuProductos.selectedItem.toString(),
                        precioUnitario,
                        cantidadText.text.toString().toInt()
                    )
                )
                actualizarDatos()
            }

    }

    fun crearOrden() {
        // Fecha del pedido
        val fechaPedido = Calendar.getInstance().time
        Log.i("fecha", fechaPedido.toString())
        // Total
        // Calificacion
        // Estado
        // Usuario
        // Restaurante
        // Productos
        /*
        // Nueva orden
        val nuevaOrden = hashMapOf<String, Any>(

        )

        // Escritura en la base de datos
        val db = Firebase.firestore
        val referencia = db.collection("orden")
        referencia
            .add(nuevaOrden)
            .addOnSuccessListener {
                // CLEAR
            }
            .addOnFailureListener {
                // TODO mensaje de error
            }
         */
    }

    fun actualizarDatos() {
        // ListView
        val listViewAdapter = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, productosAgregados
        )
        val listaProductosAgregados = findViewById<ListView>(R.id.lv_lista_productos)
        listaProductosAgregados.adapter = listViewAdapter

        // Total
        val totalPedido = findViewById<TextView>(R.id.tv_total_pedido)
        totalPedido.text = "Total del pedido: $${calcularTotalPedido()}"
    }

    fun calcularTotalPedido(): Float {
        return productosAgregados
            .map{it.precioUnitario * it.cantidad}
            .reduce { acc, precio -> return@reduce (acc + precio)}
    }

    // Menu Contextual
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        val inflater = menuInflater
        inflater.inflate(R.menu.context_menu_productos_orden, menu)

        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val id = info.position
        posicionItemSeleccionado = id
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            // Eliminar
            R.id.menu_eliminar -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Eliminar producto")
                builder.setMessage("¿Esta seguro que quiere eliminar el producto ${productosAgregados[posicionItemSeleccionado].producto}?")
                builder.setPositiveButton(
                    "Si",
                    DialogInterface.OnClickListener { dialog, which ->
                        productosAgregados.removeAt(posicionItemSeleccionado)
                        actualizarDatos()
                    }
                )
                builder.create().show()
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

}
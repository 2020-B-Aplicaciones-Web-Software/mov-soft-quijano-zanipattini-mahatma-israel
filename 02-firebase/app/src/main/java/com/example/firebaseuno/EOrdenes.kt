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
import com.example.firebaseuno.entities.Orden
import com.example.firebaseuno.entities.Producto
import com.example.firebaseuno.entities.Restaurante
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class EOrdenes : AppCompatActivity() {

    var posicionItemSeleccionado = 0
    val productosAgregados: MutableList<Producto> = ArrayList()
    val listaRestaurantes: MutableList<Restaurante> = ArrayList()
    val listaProductos: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eordenes)

        val listaProductosAgregados = findViewById<ListView>(R.id.lv_lista_productos)
        registerForContextMenu(listaProductosAgregados)

        val db = Firebase.firestore
        val referenciaRestaurantes = db.collection("restaurante")
        val referenciaProductos = db.collection("producto")

        // Restaurantes
        val menuRestaurantes = findViewById<View>(R.id.sp_restaurantes) as Spinner
        referenciaRestaurantes.get()
            .addOnSuccessListener { result ->
                result.forEach { listaRestaurantes.add(
                    Restaurante(
                        it.data["nombre"].toString(),
                        it.id
                    )
                ) }
                val adapterRestaurantes = ArrayAdapter(
                    this, android.R.layout.simple_spinner_item, listaRestaurantes
                )
                adapterRestaurantes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                menuRestaurantes.adapter = adapterRestaurantes
            }

        // Productos
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

    fun agregarProducto(productosAgregados: MutableList<Producto>) {
        val menuProductos = findViewById<View>(R.id.sp_productos) as Spinner
        val cantidadText = findViewById<EditText>(R.id.et_cantidad_producto)

        val db = Firebase.firestore
        val referenciaProductos = db.collection("producto")
        var precioUnitario: Float = 0.0f
        var uidProducto: String = ""
        referenciaProductos
            .whereEqualTo("nombre", menuProductos.selectedItem.toString())
            .get()
            .addOnSuccessListener { docs ->
                docs.forEach {
                    precioUnitario = it.data["precio"].toString().toFloat()
                    uidProducto = it.id
                }
                productosAgregados.add(
                    Producto(
                        menuProductos.selectedItem.toString(),
                        precioUnitario,
                        cantidadText.text.toString().toInt(),
                        uidProducto
                    )
                )
                actualizarDatos()
            }
    }

    fun crearOrden() {
        if (productosAgregados.size == 0) {
            // TODO mensaje de que esta vacio
        } else {
            // Fecha del pedido
            val fechaPedido = Calendar.getInstance().time
            // Total
            val total = calcularTotalPedido()
            // Estado
            val estadoPorDefecto = Orden.POR_RECIBIR
            // Usuario
            val usuario = BAuthUsuario.usuario!!.email
            // Restaurante
            val menuRestaurantes = findViewById<View>(R.id.sp_restaurantes) as Spinner
            val restauranteSeleccionado = listaRestaurantes[menuRestaurantes.selectedItemPosition]
            val restaurante = hashMapOf<String, Any>(
                "nombre" to restauranteSeleccionado.nombre,
                "uid" to restauranteSeleccionado.uid
            )
            // Productos
            val productos: MutableList< HashMap<String, Any> > = ArrayList()
            productosAgregados.forEach {productos.add(
                hashMapOf(
                    "nombre" to it.nombre,
                    "precio" to it.precio,
                    "cantidad" to it.cantidad,
                    "uid" to it.uid
                )
            )}

            // Nueva orden
            val nuevaOrden = hashMapOf<String, Any>(
                "fechaPedido" to fechaPedido.toString(),
                "total" to total,
                "estado" to estadoPorDefecto,
                "usuario" to usuario,
                "restaurante" to restaurante,
                "productos" to productos
            )

            // Escritura en la base de datos
            val db = Firebase.firestore
            val referencia = db.collection("orden")
            referencia
                .add(nuevaOrden)
                .addOnSuccessListener {
                    productosAgregados.clear()
                    actualizarDatos()
                    val cantidadText = findViewById<EditText>(R.id.et_cantidad_producto)
                    cantidadText.text.clear()
                }
                .addOnFailureListener {
                    // TODO mensaje de error
                }
        }
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
        if (productosAgregados.size > 0) {
            return productosAgregados
                .map{it.precio * it.cantidad}
                .reduce { acc, precio -> return@reduce (acc + precio)}
        } else
            return 0.0f
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
                builder.setMessage("¿Esta seguro que quiere eliminar el producto ${productosAgregados[posicionItemSeleccionado].nombre}?")
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
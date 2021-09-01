package com.example.firebaseuno

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EOrdenes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eordenes)

        val db = Firebase.firestore
        val referenciaRestaurantes = db.collection("restaurante")
        val referenciaProductos = db.collection("producto")

        val listaRestaurantes: MutableList<String> = ArrayList()
        val listaProductos: MutableList<String> = ArrayList()

        val menuRestaurantes = findViewById<View>(R.id.sp_restaurantes) as Spinner
        val menuProductos = findViewById<View>(R.id.sp_productos) as Spinner

        referenciaRestaurantes.get()
            .addOnSuccessListener { result ->
                result.forEach { listaRestaurantes.add(it.data["nombre"].toString()) }
                val adapterRestaurantes = ArrayAdapter(
                    this, android.R.layout.simple_spinner_item, listaRestaurantes
                )
                adapterRestaurantes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                menuRestaurantes.adapter = adapterRestaurantes
            }

        referenciaProductos.get()
            .addOnSuccessListener { result ->
                result.forEach { listaProductos.add(it.data["nombre"].toString()) }
                val adapterProductos = ArrayAdapter(
                    this, android.R.layout.simple_spinner_item, listaProductos
                )
                adapterProductos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                menuProductos.adapter = adapterProductos
            }

        val productosAgregados: MutableList<String> = ArrayList()

        val botonAdd = findViewById<Button>(R.id.btn_anadir_lista_producto)
        botonAdd.setOnClickListener { agregarProducto(productosAgregados) }
    }

    // TODO: Poner como parametro la referenciaProductos y que adentro saque el precio para multiplicarlo y se vea mejor
    fun agregarProducto(productosAgregados: MutableList<String>) {
        val menuProductos = findViewById<View>(R.id.sp_productos) as Spinner
        val cantidadText = findViewById<EditText>(R.id.et_cantidad_producto)

        productosAgregados.add("${menuProductos.selectedItem.toString()}\t${cantidadText.text.toString()}")

        val adapter = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, productosAgregados
        )

        val listaProductosAgregados = findViewById<ListView>(R.id.lv_lista_productos)
        listaProductosAgregados.adapter = adapter

    }

}
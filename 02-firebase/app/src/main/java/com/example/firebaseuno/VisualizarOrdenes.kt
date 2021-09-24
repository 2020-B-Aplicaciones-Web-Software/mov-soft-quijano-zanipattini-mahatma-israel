package com.example.firebaseuno

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import com.example.firebaseuno.entities.Orden
import com.example.firebaseuno.entities.Producto
import com.example.firebaseuno.entities.Restaurante
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class VisualizarOrdenes : AppCompatActivity() {

    // Firestore
    val db = Firebase.firestore
    val restaurantesRef = db.collection("restaurante")
    val ordenesRef = db.collection("orden")
    // Listas
    val listaOrdenes: MutableList<Orden> = ArrayList()
    val listaDocumentosID: MutableList<DocumentReference> = ArrayList()
    // Query
    var query: Query? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizar_ordenes)

        obtenerOrdenes()

        val botonCargarMas = findViewById<Button>(R.id.btn_cargar)
        botonCargarMas.setOnClickListener { obtenerOrdenes() }

        val listViewOrdenes = findViewById<ListView>(R.id.lv_ordenes_usuario)
        listViewOrdenes
            .setOnItemLongClickListener { parent, view, position, id ->
                val builder = AlertDialog.Builder(this)
                builder.setTitle("¿Como califica el servicio de entrega?")

                val opciones = resources.getStringArray(R.array.string_array_opciones_calificacion)
                var seleccion: Int = Orden.MUY_BUENO
                builder.setSingleChoiceItems(
                    opciones, 0,
                    { dialog, which ->
                        seleccion = Orden.CALIFICACIONES[which]
                    }
                )

                builder.setPositiveButton(
                    "Aceptar",
                    DialogInterface.OnClickListener { dialog, which ->
                        actualizarCalificacion(
                            seleccion,
                            listaDocumentosID[position]
                        )
                        actualizarCalificacionPromedioRestaurante(
                            seleccion,
                            listaOrdenes[position].restaurante
                        )
                        listaOrdenes.removeAt(position)
                        listaDocumentosID.removeAt(position)
                        actualizarListViewOrdenes()
                    }
                )

                builder.setNegativeButton("Cancelar", null)

                val dialogo = builder.create()
                dialogo.show()
                return@setOnItemLongClickListener true
            }
    }

    fun actualizarCalificacion(calificacion: Int, orden: DocumentReference) {
        // Actualizacion de la calificaion de la Orden
        db.runTransaction { transaction ->
            transaction.update(
                orden,
                mapOf(
                    "calificacion" to calificacion,
                    "estado" to Orden.ENTREGADO
                )
            )
        }
    }

    fun actualizarCalificacionPromedioRestaurante(calificacion: Int, restaurante: Restaurante?) {
        // Actualizacion de la calificacion promedio del Restaurante
        val restauranteDoc = restaurantesRef.document(restaurante!!.uid)
        db.runTransaction { transaction ->
            val documentoActual = transaction.get(restauranteDoc)
            val calificacionPromedio = documentoActual.get("calificacionPromedio")
            // Si los datos ya existen
            if (calificacionPromedio != null) {
                restaurante.sumatoriaCalificaciones = documentoActual.get("sumatoriaCalificaciones").toString().toInt()
                restaurante.usuariosCalificados = documentoActual.get("usuariosCalificados").toString().toInt()
            }
            restaurante.actualizarCalificacion(calificacion)
            transaction.update(
                restauranteDoc,
                mapOf(
                    "calificacionPromedio" to restaurante.calificacionPromedio,
                    "sumatoriaCalificaciones" to restaurante.sumatoriaCalificaciones,
                    "usuariosCalificados" to restaurante.usuariosCalificados
                )
            )
        }
    }

    fun obtenerOrdenes() {
        // Se crea la consulta
        val ordenesQuery = ordenesRef
            .whereEqualTo("estado", Orden.ENVIADO)
            .whereEqualTo("usuario", BAuthUsuario.usuario!!.email)
            .limit(3)   // Limitada a 3 documentos
        // Paginacion
        var tarea: Task<QuerySnapshot>? = null
        if (query == null) {
            tarea = ordenesQuery.get()
        } else {
            tarea = query!!.get()
        }

        tarea
            .addOnSuccessListener { snapshot ->
                guardarQuery(snapshot, ordenesQuery)
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
                    val orden = Orden(
                        it.data["fechaPedido"].toString(),
                        it.data["total"].toString().toFloat(),
                        it.data["estado"].toString(),
                        it.data["usuario"].toString(),
                        ArrayList(listaProductos)
                    )
                    val infoRestaurante = it.data["restaurante"] as HashMap<String, Any>
                    val restaurante = Restaurante(
                        infoRestaurante["nombre"].toString(),
                        infoRestaurante["uid"].toString()
                    )
                    orden.restaurante = restaurante
                    listaOrdenes.add(orden)
                    listaDocumentosID.add(ordenesRef.document(it.id))
                }
                actualizarListViewOrdenes()
            }
            .addOnFailureListener {  }
    }

    fun guardarQuery(documentSnapshots: QuerySnapshot, ordenesQuery: Query) {
        if (documentSnapshots.size() > 0) {
            val ultimoDocumento = documentSnapshots.documents[documentSnapshots.size() - 1]
            query = ordenesQuery
                .startAfter(ultimoDocumento)
        }
    }

    fun actualizarListViewOrdenes() {
        val listViewAdapter = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, listaOrdenes
        )
        val listViewOrdenes = findViewById<ListView>(R.id.lv_ordenes_usuario)
        listViewOrdenes.adapter = listViewAdapter
    }

}
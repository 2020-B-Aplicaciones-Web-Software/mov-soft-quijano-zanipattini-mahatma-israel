import java.util.*
import kotlin.collections.ArrayList

fun main() {
    println("Hola mundo")

    // Definicion de Variables
    var edadProfesor = 31   // Duck Typing

    // Especificando el tipo de variable
    var edad: Int = 10

    // Tipos de variables ----------------

    // Mutables (Re-asignar)
    var edadCachorro: Int = 0
    edadCachorro = 1
    edadCachorro = 2
    edadCachorro = 3

    // Inmutables (No re-asignar)
    val numeroCedula = 1234

    // Condicionales
    if (true) {
        //
    } else {
        //
    }

    var estadoCivil = 'P'
    // Switch
    when(estadoCivil) {
        ('C') -> {
            println("Huir")
        }
        'S' -> {
            println("Conversar")
        }
        'N' -> {
            println("Nada")
        }
        'P' -> println("Profesor")
        else -> {
            println("No tiene estado civil")
        }
    }

    var sueldo = 0
    // Ternario
    val sueldoMayor = if (sueldo > 12.2) 500 else 0

    imprimirNombre("Mahatma")

    calcularSueldo(12.00)
    calcularSueldo(13.00, 10.00)
    calcularSueldo(14.00, 11.00, 3)


    // Named Parameters
    calcularSueldo(
        bono = 3,
        // tasa 12.00 (Opcional)
        sueldo = 1000.00
    )

    calcularSueldo(
        tasa = 14.00,
        bono = 2,
        sueldo = 250.00
    )

    // Arreglos Estaticos
    val arregloEstatico: Array<Int> = arrayOf(1, 2, 3)

    // Arreglos Dinamicos
    val arregloDinamico: ArrayList<Int> = arrayListOf(1, 2, 3, 4)

    arregloDinamico.add(10)
    arregloDinamico.add(-4)

    println(arregloDinamico)


    // Operadores -> Sirven para estructuras iterables
    /*
    // FOR EACH -> Unit
    // Iterar un arreglo
    val respuestaForEach: Unit = arregloDinamico
        .forEach { valorActual: Int ->              // Parametros
            println("Valor actual ${valorActual}")
        }

    arregloDinamico.forEach {
        // it -> Tipo de dato
        println("Valor actual: ${it}")
    }

    // FOR EACH -> INDEXADO
    arregloDinamico
        .forEachIndexed{ indice: Int, valor: Int ->
            println("Valor: ${valor}, Indice: ${indice}")
        }

    // MAP -> Muta el arreglo
    // MAP -> List<...>

    println("Arreglo Dinamico:\n" + arregloDinamico)

    val respuestaMap: List<Double> = arregloDinamico
        .map { valorActual: Int ->
            return@map valorActual.toDouble()
        }

    println("Respuesta map:\n" + respuestaMap)

    val respuestaMap2: List<Double> = arregloDinamico
        .map { valorActual: Int ->
            return@map valorActual.toDouble() + 100.00
        }

    println("Respuesta map:\n" + respuestaMap2)

    val respuestaMap3: List<Date> = arregloDinamico
        .map { valorActual: Int ->
            return@map Date()
        }

    println("Respuesta map:\n" + respuestaMap3)
    */

    // Filter -> Filtra el arreglo
    /*
    val respuestaFilter: List<Int> = arregloDinamico
        .filter { valorActual: Int ->
            val mayoresATres: Boolean = valorActual > 3   // Expresion
            return@filter mayoresATres     // Boolean
        }

    println("Respuesta Filter:\n"+ respuestaFilter)
    */

    /*
    ******** Clase 06 ********
    */

    // OR -> ANY (Alguno cumple?)
    // AND -> ALL (Todos cumplen?)

    val respuestaAny: Boolean = arregloDinamico
        .any { valorActual: Int ->
            return@any (valorActual > 5)
        }

    println("ANY: " + respuestaAny)

    val respuestaAll: Boolean = arregloDinamico
        .all { valorActual: Int ->
            return@all (valorActual > 5)
        }

    println("ALL: " + respuestaAll)


    // REDUCE -> Valor acumulado
    // Valor acumulado = 0 (Siepre 0 en lenguaje Kotlin)
    // [1, 2, 3, 4, 5] -> Sume todos los valores del arreglo

    // valorIteracion1 = valorEmpieza + 1 = 0 + 1 = 1 -> Iteracion 1
    // valorIteracion2 = valorIteracion1 + 2 = 1 + 2 = 3 -> Iteracion 2
    // valorIteracion3 = valorIteracion2 + 3 = 3 + 3 = 6 -> Iteracion 3
    // valorIteracion4 = valorIteracion3 + 4 = 6 + 4 = 10 -> Iteracion 4
    // valorIteracion5 = valorIteracion4 + 5 = 10 + 5 = 15 -> Iteracion 5

    val respuestaReduce: Int = arregloDinamico
        .reduce { // acumulado = 0 -> SIEMPRE EMPIENZA EN 0
            acumulado: Int, valorActual: Int ->
            return@reduce (acumulado + valorActual) // -> Logica de negocio
        }

    println(respuestaReduce)


    val respuestaReduceFold = arrayListOf<Int>(12, 15, 8, 10)
        .fold(
            100,
            {
                acumulado, valorActualIteracion ->
                return@fold acumulado - valorActualIteracion
            }
        )

    println("FOLD: " + respuestaReduceFold)


    // Concatenacion de operaciones

    val vidaActual: Double = arregloDinamico
        .map { it * 2.3}    // arreglo
        .filter {it > 20}   // arreglo
        .fold(100.00, {acc, i -> acc - i})  // valor
        .also { println(it) }   // ejecutar codigo extra

    println("Valor vida actual ${vidaActual}")



    //
    val ejemploUno = Suma(1, 2)
    val ejemploDos = Suma(null, 2)
    val ejemploTres = Suma(1, null)
    val ejemploCuatro = Suma(null, null)

    println(ejemploUno.sumar())
    println(ejemploDos.sumar())
    println(ejemploTres.sumar())
    println(ejemploCuatro.sumar())




} // Fin main


fun imprimirNombre(nombre: String): Unit {
    println("Nombre ${nombre}") // Template Strings
}


fun calcularSueldo(
    sueldo: Double,         // Requerido
    tasa: Double = 12.00,    // Opcional (por defecto)
    // Variable que puede ser null
    bono: Int? = null
): Double {
    if (bono != null) {
        return sueldo * (100 / tasa) + bono
    } else {
        return sueldo * (100 / tasa)
    }
}

// Variables que pueden ser nulas
// String -> String?
// Int -> Int?
// Date -> Date?


abstract class NumerosJava {
    protected val numeroUno: Int    // Propiedad clase
    private val numeroDos: Int      // Propiedad clase

    constructor(
        uno: Int,   // Parametros requeridos
        dos: Int    // Parametros requeridos
    ) {
        //this.numeroUno = uno
        //this.numeroDos = dos
        numeroUno = uno     // No es necesario el this
        numeroDos = dos
        println("Inicializar")
    }
}

abstract class Numeros( // Constructor Primario
    protected var numeroUno: Int,   // Propiedad clase
    protected var numeroDos: Int    // Propiedad clase
) {
    init {  // Bloque inicio del constructor primario
        println("Inicializar")
    }
}

class Suma( // Constructor
    uno: Int,   // Parametro requerido
    dos: Int,   // Parametro requerido
): Numeros( // Constructor de la superclase
    uno,    // Se envian los parametros al superconductor
    dos
) {
    init {
        this.numeroUno
        this.numeroDos
        // X -> this.uno -> NO EXISTE
        // X -> this.dos -> NO EXISTE
    }

    constructor(    // Segundo Constructor
        uno: Int?,
        dos: Int
    ) : this (
            if (uno == null) 0 else uno,
            dos
    )

    constructor(    // Tercer Constructor
        uno: Int,
        dos: Int?
    ) : this (
        uno,
        if (dos == null) 0 else dos
    )

    constructor(    // Cuarto Constructor
        uno: Int?,
        dos: Int?
    ) : this (
        if (uno == null) 0 else uno,
        if (dos == null) 0 else dos
    )

    // METODOS ------------

    // public fun sumar(): Int {
    fun sumar(): Int {
        // val total: Int = this.numeroUno + this.numeroDos
        val total: Int = numeroUno + numeroDos
        agregarHitorial(total)
        return total
    }

    // SINGLETON
    companion object {
        val historialSumas = arrayListOf<Int>()

        fun agregarHitorial(valorNuevaSuma: Int) {
            historialSumas.add(valorNuevaSuma)
            println(historialSumas)
        }
    }

}


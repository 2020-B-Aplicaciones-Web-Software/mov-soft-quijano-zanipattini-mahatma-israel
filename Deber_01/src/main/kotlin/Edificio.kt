import java.time.LocalDate

class Edificio (
    var nombre: String,
    var numeroPisos: Int,
    var areaM2: Float,
    var fechaApertura: LocalDate,
    var direccion: String
){

    var id: Int? = null
    var departamentos = mutableListOf<Departamento>()

    fun agregarDepartamento(departamento: Departamento) {
        departamentos.add(departamento)
    }

    fun imprimirDepartamentos() {
        if (departamentos.size > 0) {
            println("-------- Departamentos en $nombre: --------")
            departamentos.forEachIndexed { index, departamento ->
                println("${index+1}.-\t" + departamento)
            }
        } else {
            println("El edificio $nombre no tiene departamentos registrados!")
        }
    }

    fun getRegisterString(): String {
        return "$id,$nombre,$numeroPisos,$areaM2,$fechaApertura,$direccion"
    }

    override fun toString(): String {
        return "Edificio: $nombre\n" +
                "\tPisos: $numeroPisos\n" +
                "\tArea (m2): $areaM2\n" +
                "\tFecha de apertura: $fechaApertura\n" +
                "\tDireccion: $direccion"
    }
}
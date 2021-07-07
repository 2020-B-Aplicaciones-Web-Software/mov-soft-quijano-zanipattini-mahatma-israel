
class Departamento (
    var nombre: String,
    var numeroHabitaciones: Int,
    var numeroBanos: Int,
    var areaM2: Float,
    var valor: Float
) {

    var id: Int? = null
    var edificio: Edificio? = null

    fun getRegisterString(): String {
        return "$id,$nombre,$numeroHabitaciones,$numeroBanos,$areaM2,$valor"
    }

    fun imprimirEdificio() {
        if (edificio != null) {
            println("El departamento $nombre pertenece al edificio ${edificio?.nombre}")
        } else {
            println("El departamento $nombre no esta asignado a ningun edificio registrado!")
        }
    }

    override fun toString(): String {
        return "Departamento: $nombre\n" +
                "\tHabitaciones: $numeroHabitaciones\n" +
                "\tBanos: $numeroBanos\n" +
                "\tArea (m2): $areaM2\n" +
                "\tValor ($): $valor"
    }
}
package  com.example.actividad1_pmdm_buena.logic
import com.example.actividad1_pmdm_buena.model.DatosUsuario
import com.example.actividad1_pmdm_buena.model.Discapacidad
import com.example.actividad1_pmdm_buena.model.EstadoCivil
import com.example.actividad1_pmdm_buena.model.GrupoProfesional
import com.example.actividad1_pmdm_buena.model.ResultadoNomina
import kotlin.math.floor


const val TIPO_SEGURIDAD_SOCIAL = 0.065
const val BASE_MAXIMA_ANUAL = 61214.40
const val GASTOS_GENERALES = 2000.0
const val MINIMO_PERSONAL = 5550.0
const val MINIMO_EDAD_65 = 1150.0
const val MINIMO_EDAD_75 = 1400.0
const val REDUCCION_MAS_DE_DOS_HIJOS = 600.0
const val LIMITE_TOPE_43 = 35200.0
const val PORCENTAJE_TOPE = 0.43
const val FACTOR_HIJOS_CASADO = 0.5
const val EDAD_65 = 65
const val EDAD_75 = 75

// Escala de retención IRPF 2026: inicio de cada tramo
const val TRAMO_1_LIMITE = 12450.0
const val TRAMO_2_LIMITE = 20200.0
const val TRAMO_3_LIMITE = 35200.0
const val TRAMO_4_LIMITE = 60000.0
const val TRAMO_5_LIMITE = 300000.0

// Cuota acumulada al empezar cada tramo
const val CUOTA_HASTA_TRAMO_1 = 2365.50
const val CUOTA_HASTA_TRAMO_2 = 4225.50
const val CUOTA_HASTA_TRAMO_3 = 8725.50
const val CUOTA_HASTA_TRAMO_4 = 17901.50
const val CUOTA_HASTA_TRAMO_5 = 125901.50

// Tipo de cada tramo
const val TIPO_TRAMO_0 = 0.19
const val TIPO_TRAMO_1 = 0.24
const val TIPO_TRAMO_2 = 0.30
const val TIPO_TRAMO_3 = 0.37
const val TIPO_TRAMO_4 = 0.45
const val TIPO_TRAMO_5 = 0.47

// Caso A · Esperado: IRPF 14,09 % · neto anual 19.852,50 € · neto por paga 1.418,04 €
val usuarioA = DatosUsuario(
    salarioBruto = 25000.0,
    pagas = 14,
    edad = 30,
    grupoProfesional = GrupoProfesional.ADMINISTRATIVO,
    discapacidad = Discapacidad.NINGUNA,
    estadoCivil = EstadoCivil.SOLTERO,
    hijos = 0
)

// Caso B · Esperado: IRPF 15,63 % · neto anual 24.918,40 € · neto por paga 2.076,53 €
val usuarioB = DatosUsuario(
    salarioBruto = 32000.0,
    pagas = 12,
    edad = 40,
    grupoProfesional = GrupoProfesional.TECNICO,
    discapacidad = Discapacidad.NINGUNA,
    estadoCivil = EstadoCivil.CASADO,
    hijos = 2
)

// Caso C · Esperado: IRPF 0,00 % · neto anual 17.765,00 € · neto por paga 1.268,93 €
val usuarioC = DatosUsuario(
    salarioBruto = 19000.0,
    pagas = 14,
    edad = 50,
    grupoProfesional = GrupoProfesional.OPERARIO,
    discapacidad = Discapacidad.ENTRE_33_Y_65,
    estadoCivil = EstadoCivil.DIVORCIADO,
    hijos = 1
)

fun escala (cantidad : Double) : Double {

    return when {
        cantidad > TRAMO_5_LIMITE -> CUOTA_HASTA_TRAMO_5 + (cantidad - TRAMO_5_LIMITE) * TIPO_TRAMO_5
        cantidad > TRAMO_4_LIMITE -> CUOTA_HASTA_TRAMO_4 + (cantidad - TRAMO_4_LIMITE) * TIPO_TRAMO_4
        cantidad > TRAMO_3_LIMITE -> CUOTA_HASTA_TRAMO_3 + (cantidad - TRAMO_3_LIMITE) * TIPO_TRAMO_3
        cantidad > TRAMO_2_LIMITE -> CUOTA_HASTA_TRAMO_2 + (cantidad - TRAMO_2_LIMITE) * TIPO_TRAMO_2
        cantidad > TRAMO_1_LIMITE -> CUOTA_HASTA_TRAMO_1 + (cantidad - TRAMO_1_LIMITE) * TIPO_TRAMO_1

        cantidad > 0.0 -> cantidad * TIPO_TRAMO_0
        else -> 0.0

    }
}

fun baseMinimaAnual (grupoProfesional: GrupoProfesional) : Double{
    return when (grupoProfesional){
        GrupoProfesional.DIRECTIVO -> 23871.60
        GrupoProfesional.TECNICO -> 19796.40
        GrupoProfesional.ADMINISTRATIVO -> 17092.80
        GrupoProfesional.OPERARIO -> 17092.80
    }
}

fun extraGastosDiscapacidad (discapacidad: Discapacidad) : Double{
    return when (discapacidad){
        Discapacidad.NINGUNA -> 0.0
        Discapacidad.ENTRE_33_Y_65 -> 3500.0
        Discapacidad.MAYOR_65 -> 7750.0
    }
}
fun minimoDiscapacidad (discapacidad: Discapacidad) : Double{
    return when (discapacidad){
        Discapacidad.NINGUNA -> 0.0
        Discapacidad.ENTRE_33_Y_65 -> 3000.0
        Discapacidad.MAYOR_65 -> 12000.0
    }
}

fun limiteExento (estadoCivil: EstadoCivil, hijos : Int) : Double {
    return if (estadoCivil != EstadoCivil.CASADO && hijos >=1) {
        when (hijos) {

            1 -> 17644.0
            else -> 18694.0
        }
    } else {
        when (hijos) {
            0 -> 15876.0
            1 -> 16342.0
            else -> 16867.0
        }
    }
}

fun reduccionTrabajo (rendimientoNeto : Double) : Double {
    return when {
        rendimientoNeto > 19747.50 -> 0.0
        rendimientoNeto > 17673.52 -> 2364.34 - 1.14 * (rendimientoNeto - 17673.52)
        rendimientoNeto > 14852.0 -> 7302.0 - 1.75 * (rendimientoNeto - 14852.0)
        else -> 7302.0
    }
}

fun minimoPorHijos (hijos: Int, estadoCivil: EstadoCivil) : Double {

    var total = 0.0

    for (posicion in 1.. hijos ){
        total += when (posicion) {
            1 -> 2400.0
            2 -> 2700.0
            3 -> 4000.0
            else -> 4500.0
        }
    }

    if (estadoCivil == EstadoCivil.CASADO)
        total *= FACTOR_HIJOS_CASADO

    return total
}


fun calcularNomina (datos : DatosUsuario ) : ResultadoNomina {

    val baseCotizacion = datos.salarioBruto.coerceIn(baseMinimaAnual(datos.grupoProfesional), BASE_MAXIMA_ANUAL)
    val seguridadSocial = baseCotizacion * TIPO_SEGURIDAD_SOCIAL
    val rendimientoNeto = datos.salarioBruto - seguridadSocial
    val gastos = (GASTOS_GENERALES + extraGastosDiscapacidad(datos.discapacidad)).coerceAtMost(rendimientoNeto)
    val reduccion = reduccionTrabajo(rendimientoNeto)

    val extraHijos = if (datos.hijos > 2) REDUCCION_MAS_DE_DOS_HIJOS else 0.0
    val base = (rendimientoNeto - gastos - reduccion - extraHijos).coerceAtLeast(0.0)

    val extra65 = if (datos.edad >= EDAD_65) MINIMO_EDAD_65 else 0.0
    val extra75 = if (datos.edad >= EDAD_75) MINIMO_EDAD_75 else 0.0
    val minimo = MINIMO_PERSONAL + extra65 + extra75 +
            minimoDiscapacidad(datos.discapacidad) +
            minimoPorHijos(datos.hijos, datos.estadoCivil)

    val cuotaInicial = (escala(base) - escala(minimo)).coerceAtLeast(0.0)

    val limite = limiteExento(datos.estadoCivil, datos.hijos)
    val tope = ((datos.salarioBruto - limite) * PORCENTAJE_TOPE).coerceAtLeast(0.0)
    val cuotaConTope = if (datos.salarioBruto <= LIMITE_TOPE_43) minOf(cuotaInicial, tope) else cuotaInicial
    val cuota = if (datos.salarioBruto <= limite) 0.0 else cuotaConTope

    val tipo = floor(cuota / datos.salarioBruto * 100 * 100) / 100
    val retencion = datos.salarioBruto * tipo / 100
    val netoAnual = datos.salarioBruto - seguridadSocial - retencion
    val netoPorPaga = netoAnual / datos.pagas.toDouble()

    return ResultadoNomina(
        salarioBruto = datos.salarioBruto,
        porcentajeIrpf = tipo,
        retencionIrpf = retencion,
        deducciones = seguridadSocial,
        salarioNeto = netoAnual,
        netoPorPaga = netoPorPaga
    )
}










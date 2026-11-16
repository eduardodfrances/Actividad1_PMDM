package com.example.actividad1_pmdm_buena.logic

import com.example.actividad1_pmdm_buena.model.DatosUsuario
import com.example.actividad1_pmdm_buena.model.Discapacidad
import com.example.actividad1_pmdm_buena.model.EstadoCivil
import com.example.actividad1_pmdm_buena.model.GrupoProfesional
import com.example.actividad1_pmdm_buena.model.ResultadoNomina
import kotlin.math.floor

// Seguridad Social y reducciones generales (2026)
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

// Aplica la escala progresiva del IRPF a una cantidad y devuelve la cuota
fun escala(cantidad: Double): Double {
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

// Base mínima anual de cotización según el grupo profesional
fun baseMinimaAnual(grupoProfesional: GrupoProfesional): Double {
    return when (grupoProfesional) {
        GrupoProfesional.DIRECTIVO -> 23871.60
        GrupoProfesional.TECNICO -> 19796.40
        GrupoProfesional.ADMINISTRATIVO -> 17092.80
        GrupoProfesional.OPERARIO -> 17092.80
    }
}

// Gastos deducibles adicionales por discapacidad
fun extraGastosDiscapacidad(discapacidad: Discapacidad): Double {
    return when (discapacidad) {
        Discapacidad.NINGUNA -> 0.0
        Discapacidad.ENTRE_33_Y_65 -> 3500.0
        Discapacidad.MAYOR_65 -> 7750.0
    }
}

// Mínimo personal adicional por discapacidad
fun minimoDiscapacidad(discapacidad: Discapacidad): Double {
    return when (discapacidad) {
        Discapacidad.NINGUNA -> 0.0
        Discapacidad.ENTRE_33_Y_65 -> 3000.0
        Discapacidad.MAYOR_65 -> 12000.0
    }
}

// Salario por debajo del cual no se aplica retención, según situación familiar
fun limiteExento(estadoCivil: EstadoCivil, hijos: Int): Double {
    return if (estadoCivil != EstadoCivil.CASADO && hijos >= 1) {
        // Familia monoparental
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

// Reducción por rendimientos del trabajo: mayor cuanto menor es el rendimiento
fun reduccionTrabajo(rendimientoNeto: Double): Double {
    return when {
        rendimientoNeto > 19747.50 -> 0.0
        rendimientoNeto > 17673.52 -> 2364.34 - 1.14 * (rendimientoNeto - 17673.52)
        rendimientoNeto > 14852.0 -> 7302.0 - 1.75 * (rendimientoNeto - 14852.0)
        else -> 7302.0
    }
}

// Mínimo por descendientes: suma por cada hijo y se reparte a la mitad si está casado
fun minimoPorHijos(hijos: Int, estadoCivil: EstadoCivil): Double {
    var total = 0.0

    for (posicion in 1..hijos) {
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

// Calcula la nómina completa: Seguridad Social, IRPF y salario neto
fun calcularNomina(datos: DatosUsuario): ResultadoNomina {

    // 1. Cotización a la Seguridad Social, limitada entre la base mínima y la máxima
    val baseCotizacion = datos.salarioBruto.coerceIn(baseMinimaAnual(datos.grupoProfesional), BASE_MAXIMA_ANUAL)
    val seguridadSocial = baseCotizacion * TIPO_SEGURIDAD_SOCIAL
    val rendimientoNeto = datos.salarioBruto - seguridadSocial

    // 2. Base para calcular la retención
    val gastos = (GASTOS_GENERALES + extraGastosDiscapacidad(datos.discapacidad)).coerceAtMost(rendimientoNeto)
    val reduccion = reduccionTrabajo(rendimientoNeto)
    val extraHijos = if (datos.hijos > 2) REDUCCION_MAS_DE_DOS_HIJOS else 0.0
    val base = (rendimientoNeto - gastos - reduccion - extraHijos).coerceAtLeast(0.0)

    // 3. Mínimo personal y familiar
    val extra65 = if (datos.edad >= EDAD_65) MINIMO_EDAD_65 else 0.0
    val extra75 = if (datos.edad >= EDAD_75) MINIMO_EDAD_75 else 0.0
    val minimo = MINIMO_PERSONAL + extra65 + extra75 +
            minimoDiscapacidad(datos.discapacidad) +
            minimoPorHijos(datos.hijos, datos.estadoCivil)

    // 4. Cuota: escala de la base menos escala del mínimo
    val cuotaInicial = (escala(base) - escala(minimo)).coerceAtLeast(0.0)

    // 5. Límite exento y tope del 43 % para salarios bajos
    val limite = limiteExento(datos.estadoCivil, datos.hijos)
    val tope = ((datos.salarioBruto - limite) * PORCENTAJE_TOPE).coerceAtLeast(0.0)
    val cuotaConTope = if (datos.salarioBruto <= LIMITE_TOPE_43) minOf(cuotaInicial, tope) else cuotaInicial
    val cuota = if (datos.salarioBruto <= limite) 0.0 else cuotaConTope

    // 6. Tipo de retención (truncado a 2 decimales) y resultados finales
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








package com.example.actividad1_pmdm_buena.model

data class ResultadoNomina(
    val salarioBruto: Double,
    val porcentajeIrpf: Double,
    val retencionIrpf: Double,
    val deducciones: Double,
    val salarioNeto: Double,
    val netoPorPaga: Double
)
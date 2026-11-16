package com.example.actividad1_pmdm_buena.model

// Resultado del cálculo que se muestra en la pantalla de resultados
data class ResultadoNomina(
    val salarioBruto: Double,
    val porcentajeIrpf: Double,
    val retencionIrpf: Double,
    val deducciones: Double,   // Cotización a la Seguridad Social
    val salarioNeto: Double,   // Neto anual
    val netoPorPaga: Double
)
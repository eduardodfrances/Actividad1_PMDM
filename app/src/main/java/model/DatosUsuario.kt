package com.example.actividad1_pmdm_buena.model

data class DatosUsuario(
    val salarioBruto: Double,
    val pagas: Int,
    val edad: Int,
    val grupoProfesional: GrupoProfesional,
    val discapacidad: Discapacidad,
    val estadoCivil: EstadoCivil,
    val hijos: Int
)

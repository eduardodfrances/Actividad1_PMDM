package com.example.actividad1_pmdm_buena.model

// Datos que introduce el usuario en el formulario y que se pasan al cálculo
data class DatosUsuario(
    val salarioBruto: Double,
    val pagas: Int,
    val edad: Int,
    val grupoProfesional: GrupoProfesional,
    val discapacidad: Discapacidad,
    val estadoCivil: EstadoCivil,
    val hijos: Int
)
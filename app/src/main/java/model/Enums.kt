package com.example.actividad1_pmdm_buena.model

// Grupo profesional: determina la base mínima de cotización
enum class GrupoProfesional {
    DIRECTIVO,
    TECNICO,
    ADMINISTRATIVO,
    OPERARIO
}

// Estado civil: afecta al mínimo por hijos y al límite exento
enum class EstadoCivil {
    SOLTERO,
    CASADO,
    SEPARADO,
    DIVORCIADO,
    VIUDO
}

// Grado de discapacidad: afecta a los gastos deducibles y al mínimo personal
enum class Discapacidad {
    NINGUNA,
    ENTRE_33_Y_65,
    MAYOR_65
}
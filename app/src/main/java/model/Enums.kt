package com.example.actividad1_pmdm_buena.model

enum class GrupoProfesional(val etiqueta: String) {
    DIRECTIVO("Directivo"),
    TECNICO("Técnico"),
    ADMINISTRATIVO("Administrativo"),
    OPERARIO("Operario")
}

enum class EstadoCivil(val etiqueta: String) {
    SOLTERO("Soltero/a"),
    CASADO("Casado/a"),
    SEPARADO("Separado/a"),
    DIVORCIADO("Divorciado/a"),
    VIUDO("Viudo/a")
}

enum class Discapacidad(val etiqueta: String) {
    NINGUNA("Sin discapacidad (menos del 33 %)"),
    ENTRE_33_Y_65("Del 33 % al 65 %"),
    MAYOR_65("65 % o más")
}
# Calculadora de salario neto

Aplicación Android que calcula el **salario neto** de un trabajador a partir de su salario bruto anual y su situación personal y familiar, aplicando la cotización a la Seguridad Social y la retención de IRPF vigentes en 2026.

Proyecto desarrollado para la **Actividad 1** del módulo *Programación Multimedia y Dispositivos Móviles* (2.º DAM).

---

## Funcionalidades

**Pantalla de datos**
- Salario bruto anual (admite decimales con coma o con punto).
- Número de pagas: 12 o 14.
- Edad, número de hijos, estado civil, grado de discapacidad y grupo profesional.
- Validación en tiempo real: los campos incorrectos se marcan en rojo con un mensaje de ayuda y el botón **Calcular** solo se activa cuando todos los datos son válidos.

**Pantalla de resultados**
- Neto por paga, destacado.
- Desglose anual: salario bruto, retención de IRPF, cotización a la Seguridad Social y salario neto anual.
- Importes con formato de moneda español (`1.418,04 €`).
- Botón **Modificar datos** y gesto de atrás del sistema para volver al formulario conservando los datos introducidos.

**Comportamiento**
- Navegación entre pantallas con paso de datos de una a otra.
- El formulario y el resultado se conservan al girar el dispositivo.

---

## Cálculo

El algoritmo se basa en el procedimiento de retenciones de la Agencia Tributaria, simplificado para la actividad.

| Paso | Concepto | Detalle |
|---|---|---|
| 1 | Seguridad Social | 6,5 % sobre la base de cotización, limitada entre la base mínima del grupo profesional y la base máxima (61.214,40 €) |
| 2 | Rendimiento neto | Salario bruto − Seguridad Social |
| 3 | Gastos deducibles | 2.000 € (+3.500 € o +7.750 € según discapacidad) |
| 4 | Reducción por trabajo | Hasta 7.302 €, decreciente según el rendimiento neto |
| 5 | Base de retención | Rendimiento neto − gastos − reducciones |
| 6 | Mínimo personal y familiar | 5.550 € + incrementos por edad (≥ 65 y ≥ 75), discapacidad e hijos (a la mitad si está casado) |
| 7 | Cuota | Escala de IRPF (19 % a 47 %) aplicada a la base, menos la escala aplicada al mínimo |
| 8 | Límites | Sin retención por debajo del límite exento; tope del 43 % para salarios de hasta 35.200 € |
| 9 | Resultado | Tipo de retención truncado a 2 decimales → retención, neto anual y neto por paga |

### Casos de prueba verificados

| Caso | Datos | Neto por paga |
|---|---|---|
| A | 25.000 € · 14 pagas · 30 años · Administrativo · soltero · 0 hijos | 1.418,04 € |
| B | 32.000 € · 12 pagas · 40 años · Técnico · casado · 2 hijos | 2.076,53 € |
| C | 19.000 € · 14 pagas · 50 años · Operario · discapacidad 33–65 % · divorciado · 1 hijo | 1.268,93 € |
| D | 70.000 € · 14 pagas · 67 años · Directivo · viudo · 0 hijos | 3.399,29 € |
| E | 17.500 € · 14 pagas · 22 años · Operario · soltero · 0 hijos | 1.118,88 € |

---

## Tecnologías

- **Kotlin**
- **Jetpack Compose** con **Material 3**
- Android Studio · SDK mínimo 24

---

## Estructura del proyecto

```
app/src/main/java/com/example/actividad1_pmdm_buena/
├── MainActivity.kt          Punto de entrada y navegación entre pantallas
├── model/
│   ├── DatosUsuario.kt      Datos introducidos por el usuario
│   ├── ResultadoNomina.kt   Resultado del cálculo
│   └── Enums.kt             Grupo profesional, estado civil y discapacidad
├── logic/
│   └── CalculadoraNomina.kt Constantes fiscales y algoritmo de cálculo
├── screens/
│   ├── PantallaDatos.kt     Formulario con validación
│   └── PantallaResultados.kt Resultado y desglose
└── ui/theme/                Tema de Material 3
```

La lógica de cálculo está separada de la interfaz: `CalculadoraNomina` recibe un `DatosUsuario` y devuelve un `ResultadoNomina`, sin depender de Compose.

---

## Decisiones técnicas

- **Navegación por estado:** un `enum` con las pantallas y un `when` en `MainActivity` decide qué pantalla se muestra. Las pantallas no se conocen entre sí; se comunican mediante parámetros y funciones (`alCalcular`, `alVolver`).
- **Estado del formulario:** cada campo usa `rememberSaveable`, de modo que los datos sobreviven a la rotación.
- **Conservación del resultado:** `ResultadoNomina` se guarda con `rememberSaveable` y un `listSaver` propio que lo convierte en una lista de valores y lo reconstruye al restaurar.
- **Estado entre pantallas:** `rememberSaveableStateHolder` conserva el estado del formulario mientras se muestra la pantalla de resultados.
- **Botón atrás:** `BackHandler` en la pantalla de resultados vuelve al formulario en lugar de cerrar la aplicación.

---

## Ejecución

1. Clonar el repositorio y abrirlo con Android Studio.
2. Esperar a que termine la sincronización de Gradle.
3. Ejecutar la configuración `app` en un emulador o dispositivo con Android 7.0 (API 24) o superior.

---

## Autor

**Eduardo Daniel Francés Rodríguez** · 2.º DAM · Curso 2026-2027

package com.example.actividad1_pmdm_buena.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.actividad1_pmdm_buena.model.DatosUsuario
import com.example.actividad1_pmdm_buena.model.Discapacidad
import com.example.actividad1_pmdm_buena.model.EstadoCivil
import com.example.actividad1_pmdm_buena.model.GrupoProfesional

// Primera pantalla: formulario con los datos del usuario
@Composable
fun PantallaDatos(
    alCalcular: (DatosUsuario) -> Unit,
    modifier: Modifier = Modifier
) {
    // Salario: acepta coma o punto decimal y debe ser mayor que 0
    var textoSalario by rememberSaveable { mutableStateOf("") }
    val salario = textoSalario.replace(',', '.').toDoubleOrNull()
    val salarioValido = salario != null && salario > 0
    val errorSalario = textoSalario.isNotEmpty() && !salarioValido

    // Edad: entre 16 y 100 años
    var textoEdad by rememberSaveable { mutableStateOf("") }
    val edad = textoEdad.toIntOrNull()
    val edadValida = edad != null && edad in 16..100
    val errorEdad = textoEdad.isNotEmpty() && !edadValida

    // Hijos: entre 0 y 20
    var textoHijos by rememberSaveable { mutableStateOf("") }
    val hijos = textoHijos.toIntOrNull()
    val hijosValido = hijos != null && hijos in 0..20
    val errorHijos = textoHijos.isNotEmpty() && !hijosValido

    // Selectores con su valor por defecto
    var pagas by rememberSaveable { mutableStateOf(14) }
    var estadoCivil by rememberSaveable { mutableStateOf(EstadoCivil.SOLTERO) }
    var grupoProfesional by rememberSaveable { mutableStateOf(GrupoProfesional.ADMINISTRATIVO) }
    var discapacidad by rememberSaveable { mutableStateOf(Discapacidad.NINGUNA) }

    // El botón Calcular solo se activa si todos los campos son válidos
    val formularioValido = salarioValido && edadValida && hijosValido

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding() // Deja hueco para el teclado y permite hacer scroll hasta el final
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Calcula tu salario neto", style = MaterialTheme.typography.headlineSmall)

        // Tarjeta: datos económicos
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("Datos económicos", style = MaterialTheme.typography.titleMedium)

                OutlinedTextField(
                    value = textoSalario,
                    onValueChange = { textoSalario = it },
                    label = { Text("Salario bruto anual") },
                    suffix = { Text("€") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    isError = errorSalario,
                    supportingText = { if (errorSalario) Text("Introduce un salario mayor que 0") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Número de pagas
                Text("Número de pagas", style = MaterialTheme.typography.titleSmall)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = pagas == 12,
                        onClick = { pagas = 12 })
                    Text("12 pagas")

                    RadioButton(
                        selected = pagas == 14,
                        onClick = { pagas = 14 })
                    Text("14 pagas")
                }
            }
        }

        // Tarjeta: datos personales
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("Datos personales", style = MaterialTheme.typography.titleMedium)

                OutlinedTextField(
                    value = textoEdad,
                    onValueChange = { textoEdad = it },
                    label = { Text("Edad") },
                    suffix = { Text("años") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    isError = errorEdad,
                    supportingText = { if (errorEdad) Text("Edad entre 16 y 100 años") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = textoHijos,
                    onValueChange = { textoHijos = it },
                    label = { Text("Número de hijos") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    isError = errorHijos,
                    supportingText = { if (errorHijos) Text("Entre 0 y 20 hijos") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Estado civil
                Column {
                    Text("Estado civil", style = MaterialTheme.typography.titleSmall)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = estadoCivil == EstadoCivil.SOLTERO,
                            onClick = { estadoCivil = EstadoCivil.SOLTERO })
                        Text("Soltero/a")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = estadoCivil == EstadoCivil.CASADO,
                            onClick = { estadoCivil = EstadoCivil.CASADO })
                        Text("Casado/a")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = estadoCivil == EstadoCivil.SEPARADO,
                            onClick = { estadoCivil = EstadoCivil.SEPARADO })
                        Text("Separado/a")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = estadoCivil == EstadoCivil.DIVORCIADO,
                            onClick = { estadoCivil = EstadoCivil.DIVORCIADO })
                        Text("Divorciado/a")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = estadoCivil == EstadoCivil.VIUDO,
                            onClick = { estadoCivil = EstadoCivil.VIUDO })
                        Text("Viudo/a")
                    }
                }

                // Grado de discapacidad
                Column {
                    Text("Grado de discapacidad", style = MaterialTheme.typography.titleSmall)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = discapacidad == Discapacidad.NINGUNA,
                            onClick = { discapacidad = Discapacidad.NINGUNA })
                        Text("Sin discapacidad")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = discapacidad == Discapacidad.ENTRE_33_Y_65,
                            onClick = { discapacidad = Discapacidad.ENTRE_33_Y_65 })
                        Text("Del 33 % al 65 %")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = discapacidad == Discapacidad.MAYOR_65,
                            onClick = { discapacidad = Discapacidad.MAYOR_65 })
                        Text("65 % o más")
                    }
                }

                // Grupo profesional
                Column {
                    Text("Grupo profesional", style = MaterialTheme.typography.titleSmall)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = grupoProfesional == GrupoProfesional.DIRECTIVO,
                            onClick = { grupoProfesional = GrupoProfesional.DIRECTIVO })
                        Text("Directivo")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = grupoProfesional == GrupoProfesional.TECNICO,
                            onClick = { grupoProfesional = GrupoProfesional.TECNICO })
                        Text("Técnico")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = grupoProfesional == GrupoProfesional.ADMINISTRATIVO,
                            onClick = { grupoProfesional = GrupoProfesional.ADMINISTRATIVO })
                        Text("Administrativo")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = grupoProfesional == GrupoProfesional.OPERARIO,
                            onClick = { grupoProfesional = GrupoProfesional.OPERARIO })
                        Text("Operario")
                    }
                }
            }
        }

        // Crea el DatosUsuario con lo introducido y lo envía al cálculo.
        // Los !! son seguros porque el botón solo está activo si los campos son válidos.
        Button(
            onClick = {
                alCalcular(
                    DatosUsuario(
                        salarioBruto = salario!!,
                        pagas = pagas,
                        edad = edad!!,
                        grupoProfesional = grupoProfesional,
                        discapacidad = discapacidad,
                        estadoCivil = estadoCivil,
                        hijos = hijos!!
                    )
                )
            },
            enabled = formularioValido,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calcular")
        }
    }
}
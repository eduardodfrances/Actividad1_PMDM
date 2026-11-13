package com.example.actividad1_pmdm_buena.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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

@Composable
fun PantallaDatos(
    alCalcular: (DatosUsuario) -> Unit,
    modifier: Modifier = Modifier
) {
    // Salario
    var textoSalario by rememberSaveable { mutableStateOf("") }
    val salario = textoSalario.replace(',', '.').toDoubleOrNull()
    val salarioValido = salario != null && salario > 0
    val errorSalario = textoSalario.isNotEmpty() && !salarioValido

    // Edad
    var textoEdad by rememberSaveable { mutableStateOf("") }
    val edad = textoEdad.toIntOrNull()
    val edadValida = edad != null && edad in 16..100
    val errorEdad = textoEdad.isNotEmpty() && !edadValida

    // Hijos
    var textoHijos by rememberSaveable { mutableStateOf("") }
    val hijos = textoHijos.toIntOrNull()
    val hijosValido = hijos != null && hijos in 0..20
    val errorHijos = textoHijos.isNotEmpty() && !hijosValido

    // Selectores
    var pagas by rememberSaveable { mutableStateOf(14) }
    var estadoCivil by rememberSaveable { mutableStateOf(EstadoCivil.SOLTERO) }
    var grupoProfesional by rememberSaveable { mutableStateOf(GrupoProfesional.ADMINISTRATIVO) }
    var discapacidad by rememberSaveable { mutableStateOf(Discapacidad.NINGUNA) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Calcula tu salario neto", style = MaterialTheme.typography.headlineSmall)

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
                    onClick = { estadoCivil = EstadoCivil.SOLTERO }
                )
                Text("Soltero/a")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = estadoCivil == EstadoCivil.CASADO,
                    onClick = { estadoCivil = EstadoCivil.CASADO }
                )
                Text("Casado/a")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = estadoCivil == EstadoCivil.SEPARADO,
                    onClick = { estadoCivil = EstadoCivil.SEPARADO }
                )
                Text("Separado/a")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = estadoCivil == EstadoCivil.DIVORCIADO,
                    onClick = { estadoCivil = EstadoCivil.DIVORCIADO }
                )
                Text("Divorciado/a")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = estadoCivil == EstadoCivil.VIUDO,
                    onClick = { estadoCivil = EstadoCivil.VIUDO }
                )
                Text("Viudo/a")
            }
        }

        // Grado de discapacidad
        Column {
            Text("Grado de discapacidad", style = MaterialTheme.typography.titleSmall)
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = discapacidad == Discapacidad.NINGUNA,
                    onClick = { discapacidad = Discapacidad.NINGUNA }
                )
                Text("Sin discapacidad")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = discapacidad == Discapacidad.ENTRE_33_Y_65,
                    onClick = { discapacidad = Discapacidad.ENTRE_33_Y_65 }
                )
                Text("Del 33 % al 65 %")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = discapacidad == Discapacidad.MAYOR_65,
                    onClick = { discapacidad = Discapacidad.MAYOR_65 }
                )
                Text("65 % o más")
            }
        }

        // Grupo profesional
        Column {
            Text("Grupo profesional", style = MaterialTheme.typography.titleSmall)
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = grupoProfesional == GrupoProfesional.DIRECTIVO,
                    onClick = { grupoProfesional = GrupoProfesional.DIRECTIVO }
                )
                Text("Directivo")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = grupoProfesional == GrupoProfesional.TECNICO,
                    onClick = { grupoProfesional = GrupoProfesional.TECNICO }
                )
                Text("Técnico")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = grupoProfesional == GrupoProfesional.ADMINISTRATIVO,
                    onClick = { grupoProfesional = GrupoProfesional.ADMINISTRATIVO }
                )
                Text("Administrativo")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = grupoProfesional == GrupoProfesional.OPERARIO,
                    onClick = { grupoProfesional = GrupoProfesional.OPERARIO }
                )
                Text("Operario")
            }
        }

        Column {
            Text("Número de pagas", style = MaterialTheme.typography.titleSmall)
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = pagas == 12,
                    onClick = { pagas =12 }
                )
                Text("12 pagas")

                RadioButton(
                    selected = pagas == 14,
                    onClick = { pagas =14 }
                )
                Text("14 pagas")

            }
        }
    }
}
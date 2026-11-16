package com.example.actividad1_pmdm_buena.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.actividad1_pmdm_buena.model.ResultadoNomina
import java.text.NumberFormat
import java.util.Locale

// Segunda pantalla: muestra el resultado del cálculo
@Composable
fun PantallaResultados(
    resultado: ResultadoNomina,
    alVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    // El gesto de atrás del sistema vuelve al formulario en lugar de cerrar la app
    BackHandler { alVolver() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Tu nómina", style = MaterialTheme.typography.headlineSmall)

        // Tarjeta destacada: neto por paga
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Neto por paga", style = MaterialTheme.typography.labelLarge)
                Text(
                    formatoEuros(resultado.netoPorPaga),
                    style = MaterialTheme.typography.displaySmall
                )
            }
        }

        // Tarjeta: desglose anual
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Desglose anual", style = MaterialTheme.typography.titleMedium)

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Salario bruto anual")
                    Text(formatoEuros(resultado.salarioBruto))
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Retención IRPF")
                    Text("-" + formatoEuros(resultado.retencionIrpf))
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Seguridad Social")
                    Text("-" + formatoEuros(resultado.deducciones))
                }

                HorizontalDivider()

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Salario neto anual", fontWeight = FontWeight.Bold)
                    Text(formatoEuros(resultado.salarioNeto), fontWeight = FontWeight.Bold)
                }
            }
        }

        // Vuelve al formulario conservando los datos introducidos
        OutlinedButton(
            onClick = { alVolver() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Modificar datos")
        }
    }
}

// Formatea una cantidad como euros en formato español (1.234,56 €)
fun formatoEuros(salario: Double): String {
    val formato = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-ES"))
    return formato.format(salario)
}
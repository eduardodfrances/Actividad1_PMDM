package com.example.actividad1_pmdm_buena

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.actividad1_pmdm_buena.logic.calcularNomina
import com.example.actividad1_pmdm_buena.model.ResultadoNomina
import com.example.actividad1_pmdm_buena.screens.PantallaDatos
import com.example.actividad1_pmdm_buena.screens.PantallaResultados
import com.example.actividad1_pmdm_buena.ui.theme.Actividad1_PMDM_BUENATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Actividad1_PMDM_BUENATheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Pantalla visible y último resultado calculado (se conservan al rotar)
                    var pantallaActual by rememberSaveable { mutableStateOf(Pantalla.DATOS) }
                    var resultado by rememberSaveable(stateSaver = ResultadoSaver) {
                        mutableStateOf<ResultadoNomina?>(null)
                    }

                    // Guarda el estado de cada pantalla aunque no se esté mostrando
                    val estadoPantallas = rememberSaveableStateHolder()

                    estadoPantallas.SaveableStateProvider(pantallaActual) {
                        // Navegación: muestra una pantalla u otra según el estado
                        when (pantallaActual) {
                            Pantalla.DATOS -> PantallaDatos(
                                alCalcular = { perfil ->
                                    resultado = calcularNomina(perfil)
                                    pantallaActual = Pantalla.RESULTADOS
                                },
                                modifier = Modifier.padding(innerPadding)
                            )

                            Pantalla.RESULTADOS -> PantallaResultados(
                                resultado = resultado!!,
                                alVolver = { pantallaActual = Pantalla.DATOS },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Pantallas de la aplicación
enum class Pantalla {
    DATOS,
    RESULTADOS
}

// Convierte el resultado en una lista de números para poder guardarlo al rotar
val ResultadoSaver = listSaver<ResultadoNomina?, Double>(
    save = { r ->
        if (r == null) emptyList()
        else listOf(
            r.salarioBruto,
            r.porcentajeIrpf,
            r.retencionIrpf,
            r.deducciones,
            r.salarioNeto,
            r.netoPorPaga
        )
    },
    restore = { lista ->
        if (lista.isEmpty()) null
        else ResultadoNomina(
            salarioBruto = lista[0],
            porcentajeIrpf = lista[1],
            retencionIrpf = lista[2],
            deducciones = lista[3],
            salarioNeto = lista[4],
            netoPorPaga = lista[5]
        )
    }
)
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
import androidx.compose.runtime.remember
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
                    var pantallaActual by remember { mutableStateOf(Pantalla.DATOS) }
                    var resultado by remember { mutableStateOf<ResultadoNomina?>(null) }

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

enum class Pantalla {
    DATOS,
    RESULTADOS
}
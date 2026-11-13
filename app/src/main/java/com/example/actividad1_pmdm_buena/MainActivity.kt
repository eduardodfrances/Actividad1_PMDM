package com.example.actividad1_pmdm_buena

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.actividad1_pmdm_buena.screens.PantallaDatos
import com.example.actividad1_pmdm_buena.ui.theme.Actividad1_PMDM_BUENATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Actividad1_PMDM_BUENATheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        PantallaDatos(
                            alCalcular = { perfil -> Log.d("PRUEBA", "Guardado: $perfil") },
                            modifier = Modifier.padding(innerPadding),
                        )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Actividad1_PMDM_BUENATheme {
        Greeting("Android")
    }
}


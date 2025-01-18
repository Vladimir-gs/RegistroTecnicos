package edu.ucne.registrotecnicos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import edu.ucne.registrotecnicos.di.TecnicoDbProvider
import edu.ucne.registrotecnicos.presentation.navigation.TecnicoNavHost
import edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RegistroTecnicosTheme {
                TecnicoNavHost(TecnicoDbProvider.getInstance(applicationContext))

            }
        }
    }
}

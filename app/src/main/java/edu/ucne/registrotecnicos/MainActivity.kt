package edu.ucne.registrotecnicos

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.registrotecnicos.presentation.navigation.AppNavHost
import edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistroTecnicosTheme {
                val navHost = rememberNavController()
                AppNavHost(
                    navController = navHost
                )
            }
        }
    }
}

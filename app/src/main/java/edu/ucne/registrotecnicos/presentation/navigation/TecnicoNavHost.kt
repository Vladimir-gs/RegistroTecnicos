package edu.ucne.registrotecnicos.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.ucne.registrotecnicos.data.local.database.TecnicoDb
import edu.ucne.registrotecnicos.presentatio.tecnico.TecnicoListScreen
import edu.ucne.registrotecnicos.presentatio.tecnico.TecnicoScreen

@Composable
fun TecnicoNavHost(tecnicoDb: TecnicoDb) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "listaTecnicos"
    ) {
        composable("listaTecnicos") {
            TecnicoListScreen(
                tecnicoDb = tecnicoDb,
                navController = navController
            )
        }
        composable("agregarTecnico") {
            TecnicoScreen(
                navController = navController,
                tecnicoDb = tecnicoDb
            )
        }
    }
}
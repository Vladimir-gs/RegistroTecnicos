package edu.ucne.registrotecnicos.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registrotecnicos.presentation.home.HomeScreen
import edu.ucne.registrotecnicos.presentation.mensaje.MensajeScreen
import edu.ucne.registrotecnicos.presentation.tecnico.TecnicoDeleteScreen
import edu.ucne.registrotecnicos.presentation.tecnico.TecnicoEditScreen
import edu.ucne.registrotecnicos.presentation.tecnico.TecnicoListScreen
import edu.ucne.registrotecnicos.presentation.tecnico.TecnicoScreen
import edu.ucne.registrotecnicos.presentation.ticket.TicketEditScreen
import edu.ucne.registrotecnicos.presentation.ticket.TicketListScreen
import edu.ucne.registrotecnicos.presentation.ticket.TicketScreen
import edu.ucne.registrotecnicos.presentation.ticket.TicketeDeleteScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen
    ) {
        //Home
        composable<Screen.HomeScreen> {
            HomeScreen(
                navController = navController
            )
        }
        //Tecnico
        composable<Screen.TecnicoList> {
            TecnicoListScreen(
                onCreate = { navController.navigate(Screen.Tecnico(0)) },
                onDelete = { navController.navigate(Screen.TecnicoDelete(it)) },
                onEdit = { navController.navigate(Screen.TecnicoEdit(it)) },
                onBack = { navController.navigateUp() }
            )
        }

        composable<Screen.Tecnico> {
            TecnicoScreen(
                goBack = { navController.navigateUp() }
            )
        }
        composable<Screen.TecnicoDelete> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.TecnicoDelete>()
            TecnicoDeleteScreen(
                tecnicoId = args.TecnicoId,
                goBack = { navController.navigateUp() }
            )
        }

        composable<Screen.TecnicoEdit> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.TecnicoEdit>()
            TecnicoEditScreen(
                tecnicoId = args.TecnicoId,
                goBack = { navController.navigateUp() }
            )
        }

        composable<Screen.TicketList> {
            TicketListScreen(
                onCreate = { navController.navigate(Screen.Ticket(0)) },
                onDelete = { navController.navigate(Screen.TicketDelete(it)) },
                onEdit = { navController.navigate(Screen.TicketEdit(it)) },
                onBack = { navController.navigateUp() },

            )
        }
        composable<Screen.Ticket> {
            TicketScreen(
                goBack = { navController.navigateUp() },
                navController = navController

            )
        }

        composable<Screen.TicketDelete> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.TicketDelete>()
            TicketeDeleteScreen(
                ticketId = args.TicketId,
                goBack = { navController.navigateUp() }
            )
        }
        composable<Screen.TicketEdit> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.TicketEdit>()
            TicketEditScreen(
                ticketId = args.TicketId,
                goBack = { navController.navigateUp() }
            )
        }

        //Mensajes
        composable<Screen.MensajeScreen> { backStackEntry ->
            MensajeScreen(
                goBack = { navController.navigateUp() },
            )
        }
    }
}

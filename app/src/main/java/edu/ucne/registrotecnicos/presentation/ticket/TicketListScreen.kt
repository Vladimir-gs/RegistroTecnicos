package edu.ucne.registrotecnicos.presentation.ticket

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import edu.ucne.registrotecnicos.data.local.database.TecnicoDb
import edu.ucne.registrotecnicos.data.local.entity.TicketsEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListScreen(
    tecnicoDb: TecnicoDb,
    navController: NavController,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val ticketList by tecnicoDb.ticketDao().getAll()
        .collectAsStateWithLifecycle(
            initialValue = emptyList<TicketsEntity>(),
            lifecycleOwner = lifecycleOwner,
            minActiveState = Lifecycle.State.STARTED
        )

    Scaffold(
        topBar = {
            TopAppBar(

                title = { Text("Lista de Ticket") },

                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { innerPadding ->
        // Uso de Modifier para evitar que el contenido invada la zona de la barra de estado
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 56.dp)
            ) {
                itemsIndexed(ticketList) { index, ticket ->
                    TicketRow(ticket)
                }
            }
        }
    }
}

@Composable
private fun TicketRow(
    ticket: TicketsEntity,
) {
    val showDialog = remember { mutableStateOf(false) }

    val prioridadTexto = when (ticket.prioridad) {
        1 -> "Alta"
        2 -> "Media"
        3 -> "Baja"
        else -> "Desconocida"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = ticket.cliente,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 7.dp)
                )
                Text(
                    text = ticket.asunto,  // Mostramos el asunto
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 7.dp)
                )
                Text(
                    text = prioridadTexto,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = ticket.descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Técnico ID: ${ticket.tecnicoId}",
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically)
            ) {
                IconButton(
                    onClick = { /* aqui debo colocar el codigo para editar */ },
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar Ticket")
                }
                IconButton(
                    onClick = { showDialog.value = true },
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar Ticket")
                }
            }
        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar este ticket?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog.value = false
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog.value = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}


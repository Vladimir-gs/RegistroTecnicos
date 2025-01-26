package edu.ucne.registrotecnicos.presentation.ticket

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import edu.ucne.registrotecnicos.data.local.database.TecnicoDb
import edu.ucne.registrotecnicos.data.local.entity.TecnicosEntity
import edu.ucne.registrotecnicos.data.local.entity.TicketsEntity
import edu.ucne.registrotecnicos.presentation.navigation.Screen
import edu.ucne.registrotecnicos.presentation.tecnico.TecnicoBodyScreen

@Composable
fun TicketListScreen(
    viewModel: TicketViewModel = hiltViewModel(),
    onCreate: () -> Unit,
    onDelete: (Int) -> Unit,
    onEdit: (Int) -> Unit,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TicketBodyListScreen(
        uiState = uiState,
        onCreate = onCreate,
        onDelete = onDelete,
        onEdit = onEdit,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketBodyListScreen(
    uiState: UiState,
    onCreate: () -> Unit,
    onDelete: (Int) -> Unit,
    onEdit: (Int) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Tickets") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(red = 102, green = 79, blue = 163, alpha = 255),
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreate
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Ticket")
            }
        }
    ) { innerPadding ->
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
                items(uiState.tickets) { ticket ->
                    TicketRow(
                        ticket = ticket,
                        tecnicos = uiState.tecnicos,
                        onEditTicket = onEdit,
                        onDeleteTicket = onDelete
                    )
                }
            }
        }
    }
}

@Composable
private fun TicketRow(
    ticket: TicketsEntity,
    tecnicos: List<TecnicosEntity>,
    onEditTicket: (Int) -> Unit,
    onDeleteTicket: (Int) -> Unit,
) {
    val nombreTecnico = tecnicos.find { tecnico ->
        tecnico.tecnicosId == ticket.tecnicoId
    }?.nombre ?: "Sin Tecnico"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
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
                    text = "Fecha: ",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = "Nombre: ${ticket.cliente}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = "Asunto: ${ticket.asunto}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = "Prioridad: ${ticket.prioridad}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = "Descripción: ${ticket.descripcion}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = "Técnico: $nombreTecnico",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically)
            ) {
                IconButton(
                    onClick = { onEditTicket(ticket.ticketId!!) },
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar Ticket")
                }
                IconButton(
                    onClick = { onDeleteTicket(ticket.ticketId!!) },
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar Ticket")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TicketListScreenPreview() {
    val sampleTickets = listOf(
        TicketsEntity(
            ticketId = 1,
            cliente = "Juan",
            asunto = "Aire",
            descripcion = "BHablalba",
            prioridad = 1,
            tecnicoId = 101
        ),
        TicketsEntity(
            ticketId = 2,
            cliente = "Miguel",
            asunto = "Carro",
            descripcion = "Describir todo aca",
            prioridad = 2,
            tecnicoId = 102
        )
    )
    TicketBodyListScreen(
        uiState = UiState(tickets = sampleTickets),
        onCreate = { },
        onDelete = { },
        onBack = {},
        onEdit = { }
    )
}

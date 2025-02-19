package edu.ucne.registrotecnicos.presentation.ticket

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnicos.data.local.entity.TecnicosEntity
import edu.ucne.registrotecnicos.data.local.entity.TicketsEntity
import java.text.SimpleDateFormat
import java.util.Locale

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
    var expanded by remember { mutableStateOf(false) }
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

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
                    text = "Fecha: ${ticket.fecha?.let { dateFormat.format(it) }}",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = "Nombre: ${ticket.cliente}",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = "Asunto: ${ticket.asunto}",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = "Prioridad: ${ticket.prioridad}",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = "Descripción: ${ticket.descripcion}",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = "Técnico: $nombreTecnico",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically)
            ) {
                IconButton(
                    onClick = { expanded = !expanded },
                ) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Opciones", tint = Color.Blue)
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Filled.Edit, contentDescription = "Editar")
                                Spacer(modifier = Modifier.width(2.dp))
                                Text("Editar")
                            }
                        },
                        onClick = {
                            onEditTicket(ticket.ticketId!!)
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                                Spacer(modifier = Modifier.width(2.dp))
                                Text("Eliminar")
                            }
                        },
                        onClick = {
                            onDeleteTicket(ticket.ticketId!!)
                            expanded = false
                        }
                    )
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

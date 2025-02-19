package edu.ucne.registrotecnicos.presentation.ticketApi

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import edu.ucne.registrotecnicos.data.remoto.dto.TicketDto
import edu.ucne.registrotecnicos.presentation.navigation.Screen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.Int

@Composable
fun ApiListScreen(
    viewModel: TicketApiViewModel = hiltViewModel(),
    drawerState: DrawerState,
    onCreate: () -> Unit,
    onDelete: (Int) -> Unit,
    onEdit: (Int) -> Unit,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    //Con esto mostramos un mensaje si hay algun error
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    //y con esto cargamos la lista de tickets
    LaunchedEffect(Unit) {
        viewModel.getTickets()
    }

    ApiListBodyScreen(
        uiState = uiState,
        drawerState = drawerState,
        onCreate = onCreate,
        onDelete = onDelete,
        onEdit = onEdit,
        onBack = onBack,
        refresh = { viewModel.getTickets() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiListBodyScreen(
    uiState: UiState,
    drawerState: DrawerState,
    onCreate: () -> Unit,
    onDelete: (Int) -> Unit,
    onEdit: (Int) -> Unit,
    onBack: () -> Unit,
    refresh: () -> Unit,
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista Api") },
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
                onClick = onCreate,
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Ticket")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                IconButton(
                    onClick = {
                        refresh()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(Icons.Filled.Refresh, contentDescription = "Recargar", tint = Color.Blue)
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(uiState.tickets) {
                        ApiRow(
                            ticket = it,
                            onEditTicket = onEdit,
                            onDeleteTicket = onDelete
                        )
                    }
                }
            }
            if (uiState.isloading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun ApiRow(
    ticket: TicketDto,
    onEditTicket: (Int) -> Unit,
    onDeleteTicket: (Int) -> Unit,
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

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
                    text = "Técnico: ${ticket.tecnicoId}",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            IconButton(
                onClick = { },
            ) {
                Icon(Icons.Filled.MoreVert, contentDescription = "opciones", tint = Color.Blue)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ApiListPreview() {
    val apiTickets = listOf(
        TicketDto(
            ticketId = 1,
            cliente = "Juan",
            fecha = null,
            asunto = "Aire",
            descripcion = "BHablalba",
            prioridad = 1,
            tecnicoId = 101
        ),
        TicketDto(
            ticketId = 2,
            cliente = "Miguel",
            fecha = null,
            asunto = "Carro",
            descripcion = "Describir todo aca",
            prioridad = 2,
            tecnicoId = 102
        )
    )
    ApiListBodyScreen(
        uiState = UiState(tickets = apiTickets),
        onCreate = { },
        onDelete = { },
        onBack = {},
        onEdit = { },
        drawerState = DrawerState(DrawerValue.Closed),
        refresh = { }
    )
}
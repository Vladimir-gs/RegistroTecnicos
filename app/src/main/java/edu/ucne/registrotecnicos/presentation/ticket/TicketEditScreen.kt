package edu.ucne.registrotecnicos.presentation.ticket

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnicos.presentation.tecnico.TecnicoViewModel

@Composable
fun TicketEditScreen(
    viewModel: TicketViewModel = hiltViewModel(),
    ticketId: Int,
    goBack: () -> Unit,
) {
    LaunchedEffect(ticketId) {
        viewModel.selectedTicket(ticketId)
    }
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    TicketBodyEditScreen(
        uiState = uiState.value,
        //onFechaChange = viewModel::onFechaChange,
        onClienteChange = viewModel::onClienteChange,
        onAsuntoChange = viewModel::onAsuntoChange,
        onDescripcionChange = viewModel::onDescripcionChange,
        onTecnicoChange = viewModel::onTecnicoChange,
        onPrioridadChange = viewModel::onPrioridadChange,
        onSave = viewModel::save,
        goBack = goBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketBodyEditScreen(
    uiState: UiState,
    //onFechaChange: (Date) -> Unit,
    onClienteChange: (String) -> Unit,
    onAsuntoChange: (String) -> Unit,
    onTecnicoChange: (Int) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onPrioridadChange: (Int) -> Unit,
    onSave: () -> Unit,
    goBack: () -> Unit,
) {
    //val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    //val formattedDate = uiState.fecha?.let { dateFormatter.format(it) } ?: ""

    //var showDatePicker by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Editar Ticket") },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Campo de cliente
            OutlinedTextField(
                label = { Text(text = "Cliente") },
                value = uiState.cliente,
                onValueChange = onClienteChange,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de asunto
            OutlinedTextField(
                label = { Text(text = "Asunto") },
                value = uiState.asunto,
                onValueChange = onAsuntoChange,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de descripción
            OutlinedTextField(
                label = { Text(text = "Descripción") },
                value = uiState.descripcion,
                onValueChange = onDescripcionChange,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de técnico (ID)
            OutlinedTextField(
                label = { Text("Id Tecnico") },
                value = uiState.tecnicoId.toString(),
                onValueChange = { newValue ->
                    val tecnico = newValue.toIntOrNull() ?: 0
                    onTecnicoChange(tecnico)
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                label = { Text("Prioridad") },
                value = uiState.prioridadId.toString(),
                onValueChange = { newValue ->
                    val prioridad = newValue.toIntOrNull() ?: 0
                    onPrioridadChange(prioridad)
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            Spacer(modifier = Modifier.height(8.dp))

            uiState.errorMessage?.let {
                Text(text = it, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier.padding(15.dp),
                    onClick = {
                        goBack()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Volver")
                }
                Button(
                    modifier = Modifier.padding(15.dp),
                    onClick = {
                        onSave()
                        goBack()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Guardar"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Guardar")
                }

            }
        }
    }
}


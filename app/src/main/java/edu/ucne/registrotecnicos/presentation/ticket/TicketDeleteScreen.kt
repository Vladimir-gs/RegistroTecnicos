package edu.ucne.registrotecnicos.presentation.ticket

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnicos.presentation.tecnico.TecnicoDeleteBodyScreen
import edu.ucne.registrotecnicos.presentation.tecnico.Uistate

@Composable
fun TicketeDeleteScreen(
    viewModel: TicketViewModel = hiltViewModel(),
    ticketId: Int,
    goBack: () -> Unit
){
    LaunchedEffect(ticketId){
        viewModel.selectedTicket(ticketId)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TicketDeleteBodyScreen(
        uiState = uiState,
        onDeleteTicket = viewModel::delete,
        goBack = goBack
    )
}

@Composable
fun TicketDeleteBodyScreen(
    uiState: UiState,
    onDeleteTicket: () -> Unit,
    goBack: () -> Unit){
    Scaffold(){
        innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ){
            Text(
                text = "¿Seguro que deseas eliminar?",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )
            Card(
                modifier = Modifier
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Eliminar",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Fecha: ",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Nombre: ${uiState.cliente}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Asunto: ${uiState.asunto}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Prioridad: ${uiState.prioridadId}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Tecnico: ${uiState.tecnicoId}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Descripción: ${uiState.descripcion}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        textAlign = TextAlign.Center
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = goBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Volver"
                            )
                            Text("Cancelar")
                        }
                        Button(
                            onClick = {
                                onDeleteTicket()
                                goBack()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error, // Fondo rojo
                                contentColor = MaterialTheme.colorScheme.onError // Color del texto en contraste
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar"
                            )
                            Text("Eliminar")
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TicketDeleteScreenPreview() {
    val fakeUiState = UiState(
        cliente = "Juan Pérez",
        asunto = "Problema con el sistema",
        prioridadId = 2,
        tecnicoId = 3,
        descripcion = "Se requiere una revisión urgente del sistema.",
        ticketId = 1
    )

    TicketDeleteBodyScreen(
        uiState = fakeUiState,
        onDeleteTicket = {},
        goBack = {}
    )
}

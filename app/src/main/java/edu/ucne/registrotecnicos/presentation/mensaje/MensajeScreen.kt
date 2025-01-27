package edu.ucne.registrotecnicos.presentation.mensaje

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnicos.data.local.entity.MensajeEntity
import edu.ucne.registrotecnicos.data.local.entity.TecnicosEntity

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MensajeScreen(
    viewModel: MensajeViewModel = hiltViewModel(),
    goBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MensajeBodyScreen(
        uiState = uiState,
        onMensajeChange = viewModel::onMensajeChange,
        onFechaChange = viewModel::onFechaChange,
        onTecnicoChange = viewModel::onTecnicoChange,
        goBack = goBack,
        onSave = viewModel::save
        )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MensajeBodyScreen(
    uiState: UiState,
    onMensajeChange: (String) -> Unit,
    onFechaChange: (String) -> Unit,
    onTecnicoChange: (Int) -> Unit,
    goBack: () -> Unit,
    onSave: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val messagesEndless = remember(uiState.mensajes) { uiState.mensajes }

    LaunchedEffect(messagesEndless) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Chat de Tickets") },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { goBack() }
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Scrollable messages area
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
            ) {
                if (messagesEndless.isNotEmpty()) {
                    messagesEndless.forEach { mensaje ->
                        MensajeRow(
                            tecnicos = uiState.tecnicos,
                            mensaje = mensaje
                        )
                    }
                }
            }

            // Input area fixed at bottom
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .padding(bottom = 10.dp),
            ) {
                Column {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = true }
                            .padding(8.dp),
                        label = { Text("Tecnico") },
                        value = uiState.tecnicos.firstOrNull { it.tecnicosId == uiState.tecnicoId }?.nombre
                            ?: "",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.clickable { expanded = true }
                            )
                        }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        uiState.tecnicos.forEach { tecnico ->
                            DropdownMenuItem(
                                text = { Text(tecnico.nombre) },
                                onClick = {
                                    onTecnicoChange(tecnico.tecnicosId ?: 0)
                                    expanded = false
                                }
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = uiState.mensaje,
                            onValueChange = onMensajeChange,
                            label = { Text("Escribir mensaje") },
                            modifier = Modifier.weight(1f),
                        )
                        Button(
                            onClick = { onSave() },
                            modifier = Modifier
                                .size(56.dp)
                                .aspectRatio(1f),
                            shape = CircleShape,
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Enviar",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MensajeRow(
    tecnicos: List<TecnicosEntity>,
    mensaje: MensajeEntity,
) {
    val nombreTecnico = tecnicos.find { tecnico ->
        tecnico.tecnicosId == mensaje.tecnicoId
    }?.nombre ?: "Sin Técnico"

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(red = 102, green = 79, blue = 163, alpha = 255))
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        // Nombre del técnico
                        Text(
                            text = "Téc. $nombreTecnico",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        // Fecha del mensaje
                        Text(
                            text = mensaje.fecha,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White
                        )
                    }
                }

                // Descripción o mensaje
                Text(
                    text = mensaje.mensaje,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(8.dp) // Padding para separar del borde
                )
            }
        }
        Divider()
    }

}

@Preview
@Composable
private fun MensajeBodyScreenPreview() {
    val sampleTecnicos = listOf(
        TecnicosEntity(tecnicosId = 1, nombre = "Enel"),
        TecnicosEntity(tecnicosId = 2, nombre = "Ana López"),
        TecnicosEntity(tecnicosId = 3, nombre = "Vladimir Guzman"),
    )

    val sampleMensajes = listOf(
        MensajeEntity(
            mensajeId = 1,
            mensaje = "Hola, ¿cómo estás?",
            fecha = "2025-01-25",
            tecnicoId = 1
        ),
        MensajeEntity(
            mensajeId = 2,
            mensaje = "Todo bien, gracias. ¿Y tú?",
            fecha = "2025-01-25",
            tecnicoId = 2
        ),
        MensajeEntity(
            mensajeId = 2,
            mensaje = "Termine tarde, pero termine la tarea que s lo importante 👍👍",
            fecha = "2025-01-25",
            tecnicoId = 2
        ),
    )

    MensajeBodyScreen(
        uiState = UiState(
            tecnicos = sampleTecnicos,
            mensajes = sampleMensajes,
            tecnicoId = 1,
            mensaje = "Escribir mensaje aquí"
        ),
        onMensajeChange = {},
        onFechaChange = {},
        onTecnicoChange = {},
        goBack = {},
        onSave = {}
    )
}

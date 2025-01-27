package edu.ucne.registrotecnicos.presentation.ticket

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import edu.ucne.registrotecnicos.presentation.navigation.Screen
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Composable
fun TicketScreen(
    viewModel: TicketViewModel = hiltViewModel(),
    goBack: () -> Unit,
    navController: NavHostController,

    ) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TicketBodyScreen(
        uiState = uiState,
        onFechaChange = viewModel::onFechaChange,
        onClienteChange = viewModel::onClienteChange,
        onAsuntoChange = viewModel::onAsuntoChange,
        onDescripcionChange = viewModel::onDescripcionChange,
        onTecnicoChange = viewModel::onTecnicoChange,
        onPrioridadChange = viewModel::onPrioridadChange,
        onSave = viewModel::save,
        goBack = goBack,
        navController = navController
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketBodyScreen(
    uiState: UiState,
    onFechaChange: (Date) -> Unit,
    onClienteChange: (String) -> Unit,
    onAsuntoChange: (String) -> Unit,
    onTecnicoChange: (Int) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onPrioridadChange: (Int) -> Unit,
    onSave: () -> Unit,
    goBack: () -> Unit,
    navController: NavHostController,
) {
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val formattedDate = uiState.fecha?.let { dateFormatter.format(it) } ?: ""
    val context = LocalContext.current

    var showDatePicker by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {

            CenterAlignedTopAppBar(

                title = {
                    Text(
                        "Agregar Ticket"
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(red = 102, green = 79, blue = 163, alpha = 255),
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = { Text("Fecha") },
                value = formattedDate,
                readOnly = true,
                onValueChange = {},
                trailingIcon = {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.clickable { showDatePicker = true }
                    )
                }
            )
            if (showDatePicker) {
                val calendar = Calendar.getInstance()
                val datePicker = android.app.DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val selectedDate = Calendar.getInstance().apply {
                            set(year, month, dayOfMonth)
                        }.time
                        onFechaChange(selectedDate)
                        showDatePicker = false
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                datePicker.setOnCancelListener {
                    showDatePicker = false
                }
                datePicker.show()
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                label = { Text(text = "Cliente") },
                value = uiState.cliente,
                onValueChange = onClienteChange,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                label = { Text(text = "Asunto") },
                value = uiState.asunto,
                onValueChange = onAsuntoChange,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                label = { Text(text = "Descripción") },
                value = uiState.descripcion,
                onValueChange = onDescripcionChange,
                modifier = Modifier.fillMaxWidth()
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

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true },
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
            }

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
            Spacer(modifier = Modifier.weight(1f)) // Esto empuja el siguiente botón hacia abajo

            Button(
                modifier = Modifier.padding(15.dp).fillMaxWidth(),
                onClick = {
                    navController.navigate(Screen.MensajeScreen)
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Mensajes"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Mensajes")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TicketBodyScreenPreview() {
    TicketBodyScreen(
        uiState = UiState(
            cliente = "Juan Pérez",
            fecha = Date(),
            asunto = "Reparación urgente",
            descripcion = "El equipo no enciende",
            tecnicoId = 1,
            prioridadId = 2
        ),
        onClienteChange = {},
        onAsuntoChange = {},
        onDescripcionChange = {},
        onPrioridadChange = {},
        onTecnicoChange = {},
        onSave = {},
        goBack = {},
        onFechaChange = {},
        navController = NavHostController(LocalContext.current)
    )
}

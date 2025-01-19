package edu.ucne.registrotecnicos.presentation.ticket

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import edu.ucne.registrotecnicos.data.local.database.TecnicoDb
import edu.ucne.registrotecnicos.data.local.entity.TicketsEntity
import kotlinx.coroutines.launch
import java.time.LocalDate


@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketScreen(
    navController: NavController,
    tecnicoDb: TecnicoDb,
) {
    var fecha by remember { mutableStateOf(LocalDate.now()) } // Fecha actual por defecto
    var prioridad by remember { mutableIntStateOf(0) }
    var cliente by remember { mutableStateOf("") }
    var asunto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var tecnicoId by remember { mutableIntStateOf(0) }
    var errorMessage: String? by remember { mutableStateOf(null) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(

                title = { Text("Agregar Ticket") },

                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
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
                value = cliente,
                onValueChange = { cliente = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de asunto
            OutlinedTextField(
                label = { Text(text = "Asunto") },
                value = asunto,
                onValueChange = { asunto = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de descripción
            OutlinedTextField(
                label = { Text(text = "Descripción") },
                value = descripcion,
                onValueChange = { descripcion = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de técnico (ID)
            OutlinedTextField(
                label = { Text("ID Técnico") },
                value = tecnicoId.toString(),
                onValueChange = {
                    tecnicoId = it.toIntOrNull() ?: 0
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                label = { Text("Prioridad") },
                value = prioridad.toString(),
                onValueChange = {
                    prioridad = it.toIntOrNull() ?: 0
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            Spacer(modifier = Modifier.height(8.dp))

            errorMessage?.let {
                Text(text = it, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (cliente.isBlank() || asunto.isBlank() || descripcion.isBlank() || tecnicoId == 0) {
                        errorMessage = "Todos los campos deben estar llenos y el ID de técnico debe ser válido"
                    } else {
                        scope.launch {
                            val ticket = TicketsEntity(
                                //fecha = fecha, // Fecha actual
                                prioridad = prioridad,
                                cliente = cliente,
                                asunto = asunto,
                                descripcion = descripcion,
                                tecnicoId = tecnicoId
                            )

                            tecnicoDb.ticketDao().save(ticket)

                            navController.navigateUp()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Guardar Ticket"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Guardar")
            }
        }
    }
}

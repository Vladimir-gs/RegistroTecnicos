package edu.ucne.registrotecnicos.presentation.tecnico

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import edu.ucne.registrotecnicos.data.local.database.TecnicoDb
import edu.ucne.registrotecnicos.data.local.entity.TecnicosEntity
import java.text.NumberFormat
import java.util.Locale


@Composable
fun TecnicoListScreen(
    tecnicoDb: TecnicoDb,
    navController: NavController,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val tecnicoList by tecnicoDb.tecnicoDao().getAll()
        .collectAsStateWithLifecycle(
            initialValue = emptyList<TecnicosEntity>(),
            lifecycleOwner = lifecycleOwner,
            minActiveState = Lifecycle.State.STARTED
        )
    val tecnicoViewModel = TecnicoViewModel(tecnicoDb)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("agregarTecnico") }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Técnico")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Lista de Técnicos",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(tecnicoList) { index, tecnico ->
                    TecnicoRow(tecnico) {
                        // Acción de eliminación
                        tecnicoViewModel.deleteTecnico(tecnico)
                    }
                }
            }
        }
    }
}

@Composable
private fun TecnicoRow(
    tecnico: TecnicosEntity,
    onDelete: (TecnicosEntity) -> Unit,
) {
    val showDialog = remember { mutableStateOf(false) } // Estado para mostrar el diálogo de confirmación

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = tecnico.tecnicosId.toString(),
                modifier = Modifier.weight(0.2f),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = tecnico.nombre,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = NumberFormat.getCurrencyInstance(Locale.US).format(tecnico.sueldo),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(0.5f)
            )
            Row(modifier = Modifier.weight(0.5f)) {
                IconButton(
                    onClick = {},
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar Técnico")
                }
                IconButton(
                    onClick = { showDialog.value = true }, // Muestra el diálogo de confirmación
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar Técnico")
                }
            }
        }
    }

    // El AlertDialog para confirmar la eliminación
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false }, // Si se toca fuera del diálogo, lo cierra
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar este técnico?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete(tecnico)  // Llama a la función de eliminación
                        showDialog.value = false  // Cierra el diálogo después de la eliminación
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog.value = false } // Solo cierra el diálogo sin eliminar
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TecnicoListScreenPreview() {
    val navController = rememberNavController()
    val tecnicoList = listOf(
        TecnicosEntity(tecnicosId = 1, nombre = "Técnico 1", sueldo = 2500.0),
        TecnicosEntity(tecnicosId = 2, nombre = "Técnico 2", sueldo = 3000.0),
        TecnicosEntity(tecnicosId = 3, nombre = "Técnico 3", sueldo = 3500.0)
    )

    // Componente simulado para la preview
    TecnicoListScreen(tecnicoList as TecnicoDb, navController)
}
package edu.ucne.registrotecnicos.presentatio.tecnico

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import edu.ucne.registrotecnicos.data.local.database.TecnicoDb
import edu.ucne.registrotecnicos.data.local.entity.TecnicosEntity
import java.text.NumberFormat
import java.util.Locale


@Composable
fun TecnicoListScreen(
    tecnicoDb: TecnicoDb,
    navController: NavController,
) {
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val tecnicoList by tecnicoDb.tecnicoDao().getAll()
        .collectAsStateWithLifecycle(
            initialValue = emptyList<TecnicosEntity>(),  // Especificamos el tipo explícitamente
            lifecycleOwner = lifecycleOwner,
            minActiveState = Lifecycle.State.STARTED
        )

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
                    TecnicoRow(tecnico)
                }
            }
        }
    }
}


@Composable
private fun TecnicoRow(tecnico: TecnicosEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
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
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
package edu.ucne.registrotecnicos.presentation.tecnico

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnicos.data.local.entity.TecnicosEntity
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TecnicoListScreen(
    viewModel: TecnicoViewModel = hiltViewModel(),
    onCreate: () -> Unit,
    onDelete: (Int) -> Unit,
    onEdit: (Int) -> Unit,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TecnicoBodyListScreen(
        uiState = uiState,
        onCreate = onCreate,
        onDelete = onDelete,
        onEdit = onEdit,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TecnicoBodyListScreen(
    uiState: Uistate,
    onCreate: () -> Unit,
    onDelete: (Int) -> Unit,
    onEdit: (Int) -> Unit,
    onBack: () -> Unit

    ) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Tecnicos") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(red = 102, green = 79, blue = 163, alpha = 255),
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Regresar", tint = Color.White)

                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreate
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Tecnico")
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
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.tecnicos) { tecnico ->
                    TecnicoRow(
                        tecnico = tecnico,
                        onDelete = onDelete,
                        onEdit = onEdit
                    )
                }
            }
        }
    }
}

@Composable
fun TecnicoRow(
    tecnico: TecnicosEntity,
    onDelete: (Int) -> Unit,
    onEdit: (Int) -> Unit,

    ) {
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
                    onClick = { onEdit(tecnico.tecnicosId!!) },
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar Técnico", tint = Color.Blue)
                }
                IconButton(
                    onClick = { onDelete(tecnico.tecnicosId!!)},
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar Técnico", tint = Color.Red)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun TecnicosListScreenPreview() {
    val sampleTecnicos = listOf(

        TecnicosEntity(
            tecnicosId = 1,
            nombre = "Juan",
            sueldo = 10000.0,

        ),
        TecnicosEntity(
            tecnicosId = 2,
            nombre = "Miguel",
            sueldo = 20000.0,

        )
    )
    TecnicoBodyListScreen(
        uiState = Uistate(tecnicos = sampleTecnicos),
        onCreate = {  },
        onDelete = {  },
        onEdit = {  },
        onBack = {}
    )
}

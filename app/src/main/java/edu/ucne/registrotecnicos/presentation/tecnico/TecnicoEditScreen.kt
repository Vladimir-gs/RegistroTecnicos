package edu.ucne.registrotecnicos.presentation.tecnico

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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnicos.presentation.ticket.TicketBodyEditScreen
import edu.ucne.registrotecnicos.presentation.ticket.UiState

@Composable
fun TecnicoEditScreen(
    viewModel: TecnicoViewModel = hiltViewModel(),
    tecnicoId: Int,
    goBack: () -> Unit,
) {
    LaunchedEffect(tecnicoId) {
        viewModel.selectedTecnico(tecnicoId)
    }
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    TecnicoBodyEditScreen(
        uiState = uiState.value,
        onNameChange = viewModel::onNameChange,
        onSalaryChange = viewModel::onSalaryChange,
        onSave = viewModel::save,
        goBack = goBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecnicoBodyEditScreen(
    uiState: Uistate,
    onNameChange: (String) -> Unit,
    onSalaryChange: (Double) -> Unit,
    onSave: () -> Unit,
    goBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Editar Técnico") },
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
                label = { Text(text = "Nombre") },
                value = uiState.nombre,
                onValueChange = onNameChange,
                /*nombre = it.replaceFirstChar { char ->
                    if (char.isLowerCase()) char.titlecase() else char.toString()
                }*/
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                label = { Text("Sueldo") },
                value = uiState.sueldo.toString(),
                onValueChange = { newValue ->
                    val salary = newValue.toDoubleOrNull() ?: 0.0
                    onSalaryChange(salary)
                },
//                    sueldo = it.toDoubleOrNull() ?: 0.0
//                    sueldoText = it
//                    sueldo = it.replace(",", "").toDoubleOrNull() ?: 0.0
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

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

@Preview(showBackground = true)
@Composable
private fun tecnicosBodyEditScreenPreview() {
    TecnicoBodyEditScreen(
        uiState = Uistate(
            nombre = "Juan Pérez",
            sueldo = 2500.0
        ),
        onNameChange = {},
        onSalaryChange = {},
        onSave = {},
        goBack = {}
    )
}

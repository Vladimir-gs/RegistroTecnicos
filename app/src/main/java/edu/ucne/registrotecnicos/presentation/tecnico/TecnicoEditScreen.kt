package edu.ucne.registrotecnicos.presentation.tecnico

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
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

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

            Button(
                onClick = {
                    onSave()
                    goBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Guardar",
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Guardar")
            }
        }
    }
}
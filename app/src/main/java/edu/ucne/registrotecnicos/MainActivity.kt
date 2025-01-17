package edu.ucne.registrotecnicos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Upsert
import edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    private lateinit var tecnicoDb: TecnicoDb
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        tecnicoDb = Room.databaseBuilder(
            applicationContext,
            TecnicoDb::class.java,
            "Ticket.db"
        ).fallbackToDestructiveMigration().build()

        setContent {
            RegistroTecnicosTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        TicketScreen()
                    }
                }
            }
        }
    }

    @Composable
    fun TicketScreen(
    ) {
        var nombre by remember { mutableStateOf("") }
        var sueldo by remember { mutableDoubleStateOf(0.0) }
        var sueldoText by remember { mutableStateOf("") }
        var errorMessage: String? by remember { mutableStateOf(null) }

        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(8.dp)
            ) {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {

                        OutlinedTextField(
                            label = { Text(text = "Cliente") },
                            value = nombre,
                            onValueChange = {
                                nombre = it.replaceFirstChar { char ->
                                    if (char.isLowerCase()) char.titlecase() else char.toString()
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            label = { Text("Sueldo") },
                            value = sueldoText,
                            onValueChange = {
                                sueldoText = it
                                sueldo = it.replace(",", "").toDoubleOrNull() ?: 0.0
                            },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        )
                        Spacer(modifier = Modifier.padding(2.dp))
                        errorMessage?.let {
                            Text(text = it, color = Color.Red)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            OutlinedButton(
                                onClick = {
                                    nombre = ""
                                    sueldo = 0.0
                                    sueldoText = ""
                                    errorMessage = null
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "New button"
                                )
                                Text(text = "Nuevo")
                            }
                            val scope = rememberCoroutineScope()
                            OutlinedButton(
                                onClick = {
                                    if (nombre.isBlank()) {
                                        errorMessage = "El nombre no puede estar vacío"
                                    } else {
                                        scope.launch {
                                            saveTecnico(
                                                TecnicosEntity(
                                                    nombre = nombre,
                                                    sueldo = sueldo
                                                )
                                            )
                                            nombre = ""
                                            sueldo = 0.0
                                            sueldoText = ""
                                            errorMessage = null
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "save button"
                                )
                                Text(text = "Guardar")
                            }
                        }
                    }
                }

                val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
                val ticketList by tecnicoDb.tecnicoDao().getAll()
                    .collectAsStateWithLifecycle(
                        initialValue = emptyList(),
                        lifecycleOwner = lifecycleOwner,
                        minActiveState = Lifecycle.State.STARTED
                    )
                TecnicoListScreen(ticketList)
            }
        }
    }

    private fun formatCurrency(value: Double): String {
        return NumberFormat.getCurrencyInstance(Locale.US).format(value)
    }

    @Composable
    fun TecnicoListScreen(tecnicoList: List<TecnicosEntity>) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text("Lista de Tecnicos")

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(tecnicoList) { tecnico ->
                    TecnicoRow(tecnico)
                }
            }
        }
    }

    @Composable
    private fun TecnicoRow(tecnico: TecnicosEntity) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = tecnico.tecnicosId.toString(),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = tecnico.nombre,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = formatCurrency(tecnico.sueldo),
                modifier = Modifier.weight(1f)
            )
        }
        HorizontalDivider()
    }

    private suspend fun saveTecnico(tecnico: TecnicosEntity) {
        tecnicoDb.tecnicoDao().save(tecnico)
    }

    @Entity(tableName = "Tecnicos")
    data class TecnicosEntity(
        @PrimaryKey
        val tecnicosId: Int? = null,
        val nombre: String = "",
        val sueldo: Double = 0.0,
    )

    @Dao
    interface TecnicosDao {
        @Upsert()
        suspend fun save(tecnico: TecnicosEntity)

        @Query(
            """
            Select *
            from Tecnicos
            where tecnicosId = :id
            limit 1
            """
        )
        suspend fun find(id: Int): TecnicosEntity?

        @Delete()
        suspend fun delete(tecnicosEntity: TecnicosEntity)

        @Query("Select * from Tecnicos")
        fun getAll(): Flow<List<TecnicosEntity>>
    }

    @Database(
        entities = [TecnicosEntity::class],
        version = 1,
        exportSchema = false
    )
    abstract class TecnicoDb : RoomDatabase() {
        abstract fun tecnicoDao(): TecnicosDao
    }
}

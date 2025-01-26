package edu.ucne.registrotecnicos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mensajes")
data class MensajeEntity(
    @PrimaryKey
    val mensajeId: Int? = null,
    val tecnicoId: Int = 0,
    val mensaje: String = "",
    val fecha: String = "",
)

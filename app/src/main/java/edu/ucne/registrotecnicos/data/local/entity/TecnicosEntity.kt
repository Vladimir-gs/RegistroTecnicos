package edu.ucne.registrotecnicos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tecnicos")
data class TecnicosEntity(
    @PrimaryKey
    val tecnicosId: Int? = null,
    val nombre: String = "",
    val sueldo: Double = 0.0,
)

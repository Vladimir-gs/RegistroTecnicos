package edu.ucne.registrotecnicos.data.remoto.dto

data class MensajeDto(
    val mensajeId: Int?,
    val tecnicoId: Int,
    val mensaje: String,
    val fecha: String,
)

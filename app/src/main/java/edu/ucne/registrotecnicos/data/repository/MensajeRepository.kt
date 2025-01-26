package edu.ucne.registrotecnicos.data.repository

import edu.ucne.registrotecnicos.data.local.dao.MensajeDao
import edu.ucne.registrotecnicos.data.local.entity.MensajeEntity
import javax.inject.Inject

class MensajeRepository @Inject constructor(
    private val mensajeDao: MensajeDao
) {
    suspend fun save(mensaje: MensajeEntity) = mensajeDao.save(mensaje)
    fun getAll() = mensajeDao.getAll()
    suspend fun delete(mensaje: MensajeEntity) = mensajeDao.delete(mensaje)
    suspend fun getMensaje(mensajeId: Int) = mensajeDao.find(mensajeId)
}
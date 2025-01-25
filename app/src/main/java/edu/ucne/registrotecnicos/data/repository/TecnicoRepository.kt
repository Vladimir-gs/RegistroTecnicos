package edu.ucne.registrotecnicos.data.repository

import edu.ucne.registrotecnicos.data.local.dao.TecnicosDao
import edu.ucne.registrotecnicos.data.local.entity.TecnicosEntity
import javax.inject.Inject

class TecnicoRepository @Inject constructor(
    private val tecnicoDao: TecnicosDao
){
    suspend fun save(tecnico: TecnicosEntity) = tecnicoDao.save(tecnico)
    fun getAll() = tecnicoDao.getAll()
    suspend fun delete(tecnico: TecnicosEntity) = tecnicoDao.delete(tecnico)
    suspend fun getTecnico(tecnicoId: Int) = tecnicoDao.find(tecnicoId)
}
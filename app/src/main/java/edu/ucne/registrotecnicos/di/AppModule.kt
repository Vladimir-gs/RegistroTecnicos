package edu.ucne.registrotecnicos.di

import edu.ucne.registrotecnicos.data.local.database.TecnicoDb

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideDb(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(
            appContext,
            TecnicoDb::class.java,
            "Ticket.db"
        ).fallbackToDestructiveMigration()
            .build()
    @Provides
    @Singleton
    fun provideTicketDao(tecnicoDb: TecnicoDb) = tecnicoDb.ticketDao()
    @Provides
    @Singleton
    fun provideTecnicoDao(tecnicoDb: TecnicoDb) = tecnicoDb.tecnicoDao()
    @Provides
    @Singleton
    fun provideMensajeDao(tecnicoDb: TecnicoDb) = tecnicoDb.mensajeDao()
}

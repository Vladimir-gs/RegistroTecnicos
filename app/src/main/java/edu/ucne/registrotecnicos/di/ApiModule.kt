package edu.ucne.registrotecnicos.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.ucne.registrotecnicos.data.remoto.MensajeApi
import edu.ucne.registrotecnicos.data.remoto.TecnicoApi
import edu.ucne.registrotecnicos.data.remoto.TicketApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApiModule {
    const val BASE_URL = "https://ticketapi-cabbcqfhehgxc7h7.centralus-01.azurewebsites.net/"

    @Singleton
    @Provides
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(DateAdapter())
            .build()

    @Singleton
    @Provides
    fun provideTicketApi(moshi: Moshi): TicketApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(TicketApi::class.java)
    }


}
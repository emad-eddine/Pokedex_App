package com.kichou.imad.pokdexapp.di

import com.kichou.imad.pokdexapp.data.remote.PokiApi
import com.kichou.imad.pokdexapp.repository.PokemonRepository
import com.kichou.imad.pokdexapp.util.Constant.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePokemonRepository(pokiApi: PokiApi) : PokemonRepository = PokemonRepository(pokiApi)


    @Provides
    @Singleton
    fun providePokiApi() : PokiApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PokiApi::class.java)
    }



}
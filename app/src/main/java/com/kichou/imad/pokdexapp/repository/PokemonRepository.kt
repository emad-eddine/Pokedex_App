package com.kichou.imad.pokdexapp.repository

import com.kichou.imad.pokdexapp.data.remote.PokiApi
import com.kichou.imad.pokdexapp.data.remote.response.Pokemon
import com.kichou.imad.pokdexapp.data.remote.response.PokemonList
import com.kichou.imad.pokdexapp.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject


@ActivityScoped
class PokemonRepository @Inject constructor(
    private val pokiApi: PokiApi) {

    suspend fun getPokemonList(limit : Int , offset: Int) : Resource<PokemonList>{

        val response = try {
            pokiApi.getPokemonList(limit,offset)
        }catch (e : Exception){

            return Resource.Error(message = "An unknown message")

        }
        return Resource.Success(data = response)
    }


    suspend fun getPokemonInfo(pokemonName : String) : Resource<Pokemon>{

        val response = try {
            pokiApi.getPokemonInfo(pokemonName)
        }catch (e : Exception){

            return Resource.Error(message = "An unknown message")

        }
        return Resource.Success(data = response)
    }


}
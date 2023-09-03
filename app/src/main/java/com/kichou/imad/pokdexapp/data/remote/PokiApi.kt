package com.kichou.imad.pokdexapp.data.remote

import com.kichou.imad.pokdexapp.data.remote.response.Pokemon
import com.kichou.imad.pokdexapp.data.remote.response.PokemonList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokiApi {


    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit : Int,
        @Query("offset") offset : Int,
    ) : PokemonList

    @GET("pokemon/{name}")
    suspend fun getPokemonInfo(
        @Path("name") pokemonName : String
    ) : Pokemon

}
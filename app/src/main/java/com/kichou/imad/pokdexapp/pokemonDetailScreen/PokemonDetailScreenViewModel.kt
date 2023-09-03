package com.kichou.imad.pokdexapp.pokemonDetailScreen

import androidx.lifecycle.ViewModel
import com.kichou.imad.pokdexapp.data.remote.response.Pokemon
import com.kichou.imad.pokdexapp.repository.PokemonRepository
import com.kichou.imad.pokdexapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class PokemonDetailScreenViewModel @Inject constructor(
    private val repository: PokemonRepository) : ViewModel(){


        suspend fun getPokemonInfo(pokemonName : String): Resource<Pokemon>{
            return repository.getPokemonInfo(pokemonName)
        }


}
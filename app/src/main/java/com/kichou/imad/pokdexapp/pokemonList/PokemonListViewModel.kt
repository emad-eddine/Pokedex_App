package com.kichou.imad.pokdexapp.pokemonList

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.kichou.imad.pokdexapp.data.models.PokedexListEntry
import com.kichou.imad.pokdexapp.repository.PokemonRepository
import com.kichou.imad.pokdexapp.util.Constant.PAGE_SIZE
import com.kichou.imad.pokdexapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository)   : ViewModel() {

    private var currentPage : Int = 0

    var pokemonList = mutableStateOf<List<PokedexListEntry>>(listOf())

    var loadError = mutableStateOf("")

    var isLoading = mutableStateOf(false)

    var endReached = mutableStateOf(false)


    private var cachePokemonList = listOf<PokedexListEntry>()

    private var isSearchStarting = true

    var isSearching = mutableStateOf(false)

    init {
        loadPokemonPagination()
    }


    fun searchPokemonList(querry : String){
        val listToSearch = if(!isSearchStarting){
            pokemonList.value
        }else{
            cachePokemonList
        }

        viewModelScope.launch (Dispatchers.Default){
            if(querry.isEmpty()){
                pokemonList.value = cachePokemonList
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }
            val result = listToSearch.filter {
                it.pokemonName.contains(querry.trim(),ignoreCase = true) ||
                        it.pokemonNumber.toString() == querry.trim()
            }
            if (isSearchStarting){
                cachePokemonList = pokemonList.value
                isSearchStarting = false
            }

            pokemonList.value = result
            isSearching.value = true

        }


    }





    fun loadPokemonPagination(){
        viewModelScope.launch {
            isLoading.value = true

            val result = repository.getPokemonList(PAGE_SIZE,currentPage * PAGE_SIZE)

            when(result){

                is Resource.Success ->{
                    endReached.value = currentPage * PAGE_SIZE >= result.data!!.count

                    val pokedexEntries = result.data.results.mapIndexed{ index, entry->
                        val number = if(entry.url.endsWith("/")){
                            entry.url.dropLast(1).takeLastWhile {it.isDigit()}
                        }else{
                            entry.url.takeLastWhile { it.isDigit() }
                        }

                        val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"

                        PokedexListEntry(
                            pokemonName = entry.name,
                            pokemonImgUrl = url,
                            pokemonNumber = number.toInt())
                    }

                    currentPage++

                    loadError.value = ""
                    isLoading.value = false

                    pokemonList.value += pokedexEntries



                }

                is Resource.Error ->{
                    loadError.value= result.message!!
                    isLoading.value = false
                }

                is Resource.Loading ->{

                }

            }

        }
    }





    fun calcDominantColor(drawable : Drawable , onFinish : (Color) -> Unit){

        val bmp = (drawable as BitmapDrawable).bitmap.copy(
            Bitmap.Config.ARGB_8888,true)

        Palette.from(bmp).generate(){palette ->

            palette?.dominantSwatch?.rgb?.let {colorValue ->

                onFinish(Color(colorValue))

            }

        }


    }









    }